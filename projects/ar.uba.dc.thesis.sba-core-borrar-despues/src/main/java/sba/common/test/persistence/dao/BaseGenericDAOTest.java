/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 * 
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: BaseGenericDAOTest.java,v 1.1 2008/04/30 14:17:44 cvscalab Exp $
 */

package sba.common.test.persistence.dao;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import sba.common.dao.GenericDAO;
import sba.common.persistence.BasePersistentObject;
import sba.common.test.core.BaseDummyObjectFactory;

/**
 * @author ecalabrese
 */

/**
 * @author Emiliano Calabrese
 * @version $Revision: 1.1 $ $Date: 2008/04/30 14:17:44 $
 */

public abstract class BaseGenericDAOTest<T extends BasePersistentObject> extends TestCase {

	/**
	 * Test del ciclo <code>CRUD</code>. <br/> Toma un EntityObject nuevo, lo persiste, luego lo
	 * recupera de la base, lo modifica y lo vuelve a persistir, y por ultimo lo elimina. <br/>
	 * Realiza todas las verificaciones correspondientes.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public final void testAddUpdateDeleteElement() throws Exception {
		logger.trace("BaseCrudDaoTest >> testAddUpdateDeleteElement   [start]");
		try {
			transactionTemplate = (TransactionTemplate) getAppContext().getBean(
					"transactionTemplate");
		} catch (Exception e) {
			/*
			 * fail("Debe definir el bean transactionTemplate, para esto puede " + "incluir
			 * spring/spring-BaseCrudDAOTest.xml en su appContext");
			 */
			String advertencia = " Debe definir el bean transactionTemplate, para esto puede"
					+ " incluir spring/spring-BaseCrudDAOTest.xml en su appContext";
			throw new Exception("ERROR: " + advertencia, e);
		}

		// cada test se encarga de crear el objeto a manipular
		final T newObject = this.getNewObject();
		final T objectSaved;

		objectSaved = (T) transactionTemplate.execute(new TransactionCallback() {
			public T doInTransaction(TransactionStatus ts) {
				try {
					return doInsert(newObject);
				} catch (Exception e) {
					ts.setRollbackOnly();
				}
				return null;
			}
		});

		transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				try {
					doUpdate(objectSaved);
				} catch (Exception e) {
					ts.setRollbackOnly();
				}
				return null;
			}
		});

		transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				try {
					doDelete(objectSaved);
				} catch (Exception e) {
					if (logger.isTraceEnabled()) {
						logger.trace("ERROR en BaseCrudDaoTest - doDelete in transaction: ", e);
					}
					ts.setRollbackOnly();
				}
				return null;
			}
		});

		logger.trace("BaseCrudDaoTest >> testAddUpdateDeleteElement   [end]");
	}

	final T doInsert(T newObject) {
		// SAVE: Persiste el objeto pedido a la Factory
		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> doInsert()  [start]");
			logger.trace("BaseCrudDaoTest >> doInsert     newObject: " + newObject);
		}
		T objectSaved = null;
		try {
			if (logger.isTraceEnabled())
				logger.trace("BaseCrudDaoTest >> doInsert     saving: " + newObject);

			this.dao.save(newObject);

			if (logger.isTraceEnabled())
				logger.trace("BaseCrudDaoTest >> doInsert     saved...");
			if (logger.isTraceEnabled())
				logger.trace("BaseCrudDaoTest >> doInsert     retrieving...");

			objectSaved = retrieve(newObject.getId());

			if (logger.isTraceEnabled())
				logger.trace("BaseCrudDaoTest >> doInsert     retrieved: " + objectSaved);

			assertEquals("NO ACTUALIZO EL ID!", newObject.getId(), objectSaved.getId());
			assertTrue("FALLO EL SAVE! Los objetos no coinciden segun el equals implementado! ",
					equals(objectSaved, newObject));
		} catch (Exception e) {
			logger.trace("Exception@BaseCrudDaoTest>>doInsert:", e);
			@SuppressWarnings("unused")
			String msg;
			if (e.getMessage() != null) {
				msg = e.getMessage();
			} else {
				msg = "Mensaje de excepción nulo.";
			}
			// fail("FALLO EL SAVE!\n".concat(msg));
			throw new RuntimeException("La operación SAVE falló", e);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> doInsert()  [end] return: " + objectSaved);
		}
		return objectSaved;
	}

	final void doUpdate(final T objectSaved) {
		// UPDATE: lo modifico y luego lo persisto
		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> doUpdate()  [start]");
			logger.trace("BaseCrudDaoTest >> doUpdate     objectSaved: " + objectSaved);
		}

		try {
			T objectSavedCopy = objectSaved;
			if (logger.isTraceEnabled())
				logger.trace("BaseCrudDaoTest >> doUpdate     objectSavedCopy: " + objectSavedCopy);

			this.doSomeChange(objectSavedCopy);
			this.dao.save(objectSavedCopy);

			T objectUpdated = retrieve(objectSavedCopy.getId());

			if (logger.isTraceEnabled())
				logger.trace("BaseCrudDaoTest >> doUpdate     objectUpdated: " + objectUpdated);

			assertTrue("FALLO EL UPDATE! Los objetos no coinciden segun el equals implementado!",
					equals(objectUpdated, objectSaved));

		} catch (Exception e) {
			logger.trace("Exception@BaseCrudDaoTest>>doUpdate:", e);
			@SuppressWarnings("unused")
			String msg;
			if (e.getMessage() != null) {
				msg = e.getMessage();
			} else {
				msg = "Mensaje de excepción nulo.";
			}
			// fail("FALLO EL SAVE!\n".concat(msg));
			throw new RuntimeException("La operación UPDATE falló", e);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> doUpdate()  [end]");
		}
	}

	final void doDelete(T objectUpdated) {
		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> doDelete()  [start]");
			logger.trace("BaseCrudDaoTest >> doDelete     objectUpdated: " + objectUpdated);
		}
		// DELETE: lo borro y verifico que no lo pueda recuperar
		try {
			if (logger.isTraceEnabled())
				logger.trace("BaseCrudDaoTest >> doDelete     deleting...");
			this.dao.delete(objectUpdated.getId());
			if (logger.isTraceEnabled())
				logger.trace("BaseCrudDaoTest >> doDelete     deleted...");
			/*
			 * if (logger.isTraceEnabled()) logger.trace("BaseCrudDaoTest >> doDelete
			 * retrieving..."); T objectRetrieved = retrieve(objectUpdated.getId()); if
			 * (logger.isTraceEnabled()) logger.trace("BaseCrudDaoTest >> doDelete objectRetrieved: " +
			 * objectRetrieved); assertNull( "FALLO EL DELETE! Se ha recuperado de la base un objeto
			 * no nulo!", objectRetrieved);
			 */
		} /*
			 * catch (org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException
			 * expected) { logger.trace("ExpectedException.2@BaseCrudDaoTest>>doDelete:",expected);
			 * //fail("FALLO EL DELETE!\n".concat(msg)); fail("La operación DELETE falló, debería
			 * haberse producido una HibernateObjectRetrievalFailureException"); } catch
			 * (org.hibernate.ObjectDeletedException expected) {
			 * logger.trace("ExpectedException.1@BaseCrudDaoTest>>doDelete:",expected);
			 * //fail("FALLO EL DELETE!\n".concat(msg)); fail("La operación DELETE falló, debería
			 * haberse producido una ObjectDeletedException"); }
			 */
		catch (Exception e) {
			logger.trace("Exception@BaseCrudDaoTest>>doDelete:", e);
			@SuppressWarnings("unused")
			String msg;
			if (e.getMessage() != null) {
				msg = e.getMessage();
			} else {
				msg = "Mensaje de excepción nulo.";
			}
			// fail("FALLO EL DELETE!\n".concat(msg));
			throw new RuntimeException("La operación DELETE falló", e);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> doDelete()  [end]");
		}
	}

	// final void doWork() {
	// // cada test se encarga de crear el objeto a manipular
	// T newObject = this.getNewObject();
	// T objectSaved = null;
	// T objectUpdated = null;
	//		
	// //SAVE: Persiste el objeto pedido a la Factory
	// try{
	// this.dao.save(newObject);
	// objectSaved = retrieve(newObject.getId());
	// assertEquals("NO ACTUALIZO EL ID!", newObject.getId(),
	// objectSaved.getId());
	// assertTrue(
	// "FALLO EL SAVE! Los objetos no coinciden segun el equals implementado! ",
	// equals(objectSaved, newObject));
	// }catch(Exception e){
	// String msg;
	// if(e.getMessage()!=null){
	// msg = e.getMessage();
	// }else{
	// msg = "Mensaje de excepción nulo.";
	// }
	// fail("FALLO EL SAVE!\n".concat(msg));
	// }
	//		
	// //UPDATE: lo modifico y luego lo persisto
	// try{
	// this.doSomeChange(objectSaved);
	// this.dao.save(objectSaved);
	// objectUpdated = retrieve(objectSaved.getId());
	// assertTrue(
	// "FALLO EL UPDATE! Los objetos no coinciden segun el equals
	// implementado!",
	// equals(objectUpdated, objectSaved));
	// }catch(Exception e){
	// String msg;
	// if(e.getMessage()!=null){
	// msg = e.getMessage();
	// }else{
	// msg = "Mensaje de excepción nulo.";
	// }
	// fail("FALLO EL UPDATE!\n".concat(msg));
	// }
	//
	// //DELETE: lo borro y verifico que no lo pueda recuperar
	// try{
	// this.dao.delete(objectUpdated.getId());
	// objectUpdated = retrieve(objectUpdated.getId());
	// assertNull(
	// "FALLO EL DELETE! Se ha recuperado de la base un objeto no nulo!",
	// objectUpdated);
	// }catch(Exception e){
	// String msg;
	// if(e.getMessage()!=null){
	// msg = e.getMessage();
	// }else{
	// msg = "Mensaje de excepción nulo.";
	// }
	// fail("FALLO EL DELETE!\n".concat(msg));
	// }
	// }

	/**
	 * Verifica la igualdad de dos objetos persistentes.
	 * @param objectRetrieved
	 * @param originalObject
	 * @return <code>true</code> si ambos objetos son iguales, <code>false</code> si no
	 */
	protected abstract boolean equals(T objectRetrieved, T originalObject);

	/**
	 * @throws Exception
	 * @todo Agregar aserciones
	 */
	public final void testRetrieveAll() throws Exception {
		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> testRetrieveAll()  [start]");
		}

		this.dao.findAll();

		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> testRetrieveAll()  [done]");
		}
	}

	/**
	 * Metodo que modifica el objeto de negocio para luego persistirlo y verificar que los cambios
	 * hayan surgido efecto. Idealmente deberían modificarse todos los datos.
	 * @param object
	 *            Objecto de negocio a modificar
	 */
	protected abstract void doSomeChange(T object);

	/**
	 * Obtiene un objeto de negocio con el id dado
	 * @param id
	 *            Identificador de persistencia del objeto de negocio
	 * @return Objeto de negocio con el id dado
	 */
	protected T retrieve(Long id) {
		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> retrieve()  [start]");
			logger.trace("BaseCrudDaoTest >> retrieve    id: " + id);
		}

		T obj = dao.findById(id);

		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> retrieve()  [done] return: " + obj);
		}

		return obj;
	}

	/**
	 * Devuelve el nombre del objeto a buscar en la DummyObjectFactory. Este método podría ser usado
	 * por <code>getNewObject</code>, es solo una opcion de implementación.
	 * @return nombre del objeto a buscar en la DummyObjectFactory
	 * @see #getNewObject
	 * @see BaseDummyObjectFactory
	 */
	protected abstract String getDummyName();

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> tearDown()  [start]");
		}

		super.tearDown();
		this.dao = null;

		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> tearDown()  [end]");
		}
	}

	/**
	 * Devuelve la instancia del Objeto a insertar, actualizar y eliminar
	 * @return la instancia del Objeto a insertar, actualizar y eliminar
	 */
	protected abstract T getNewObject();

	@Override
	protected void setUp() throws Exception {
		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> setup()  [start]");
		}

		ctx = getAppContext();

		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> setup()  [end]");
		}
	}

	/**
	 * Devuelve el contexto de la aplicación.
	 * @return el contexto de la aplicación
	 * @see sba.common.test.core.AppContextContainer
	 */
	protected final ApplicationContext getAppContext() {
		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> getAppContext()  [start]");
		}

		if (ctx == null) {
			ctx = createAppContext();
		}

		if (logger.isTraceEnabled()) {
			logger.trace("BaseCrudDaoTest >> getAppContext()  [end] return: " + ctx);
		}
		return ctx;
	}

	/**
	 * Crea el contexto de la aplicación levantando todos los descriptores de spring. Este contexto
	 * debe cargar el descriptor spring-BaseCrudDAOTest.xml que define el template de hibernate
	 * utilizado para crear la transacción. Se puede utilizar <code>AppContextContainer</code>
	 * para cachear el contexto
	 * @return el contexto de la aplicación
	 */
	protected abstract ApplicationContext createAppContext();

	private static ApplicationContext ctx;

	protected GenericDAO<T> dao;

	private TransactionTemplate transactionTemplate;

	private static Log logger = LogFactory.getLog(BaseGenericDAOTest.class);

}
