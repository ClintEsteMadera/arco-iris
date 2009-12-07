/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: ContactoCrudDAOTest.java,v 1.3 2008/04/30 14:17:44 cvscalab Exp $
 */

package sba.common.contacto.core.dao.hibernate;

import org.springframework.context.ApplicationContext;
import sba.common.contacto.Contacto;
import sba.common.contacto.core.ContactoAppContextContainer;
import sba.common.contacto.core.ContactoDummyFactory;
import sba.common.dao.CrudDAO;
import sba.common.test.persistence.dao.BaseCrudDAOTest;

/**
 * Clase que realiza un test de Alta, Baja y Modificaci�n de un <code>Contacto</code>
 * @author Jonathan Chiocchio
 * @version $Revision: 1.3 $ $Date: 2008/04/30 14:17:44 $
 */

public class ContactoCrudDAOTest extends BaseCrudDAOTest<Contacto> {

	@Override
	@SuppressWarnings("unchecked")
	protected void setUp() throws Exception {
		super.setUp();
		dao = (CrudDAO<Contacto>) getAppContext().getBean("contactoDAO");
	}

	@Override
	protected ApplicationContext createAppContext() {
		return ContactoAppContextContainer.getInstance().getAppContext();
	}

	@Override
	@SuppressWarnings("unused")
	protected void doSomeChange(Contacto contacto) {
	// No hace nada pues Contacto es un value object.
	}

	@Override
	protected boolean equals(Contacto contacto1, Contacto contacto2) {
		return (contacto1 == null) ? contacto2 == null : contacto1.equals(contacto2);
	}

	@Override
	protected Contacto getNewObject() {
		return ContactoDummyFactory.crearContacto();
	}

	/**
	 * No se debe utilizar este m�todo ya que al llamar a la DomicilioDummyFactory no se crean
	 * objetos nuevos, por lo que los objetos que se utilizan una vez ya quedan como persistentes,
	 * es decir, pasan a tener un id.
	 */
	@Override
	@Deprecated
	protected final String getDummyName() {
		throw new RuntimeException("No se debe utilizar este metodo");
	}
}