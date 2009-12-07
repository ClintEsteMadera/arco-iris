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
 * $Id: GenericHibernateDAO.java,v 1.1 2008/04/30 14:17:44 cvscalab Exp $
 */

package sba.common.dao.hibernate;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import sba.common.dao.GenericDAO;
import sba.common.persistence.PersistentObject;

/**
 * @author ecalabrese
 */

/**
 * @author Emiliano Calabrese
 * @version $Revision: 1.1 $ $Date: 2008/04/30 14:17:44 $
 */

public abstract class GenericHibernateDAO<T extends PersistentObject> extends HibernateDaoSupport
		implements GenericDAO<T> {

	@SuppressWarnings("unchecked")
	public GenericHibernateDAO() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public T findById(Long id) {
		return (T) getHibernateTemplate().get(getPersistentClass(), id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(getPersistentClass()));
	}

	public T save(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}

	public void delete(T entity) {
		getHibernateTemplate().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public void delete(Long id) {
		T entity = (T) getHibernateTemplate().load(getPersistentClass(), id); // crea un proxy para envolver al id
		getHibernateTemplate().delete(entity);
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	private final Class<T> persistentClass;

}
