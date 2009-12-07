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
 * $Id: GenericDAO.java,v 1.1 2008/04/30 14:17:44 cvscalab Exp $
 */

package sba.common.dao;

import java.util.List;
import sba.common.persistence.PersistentObject;

/**
 * @author Emiliano Calabrese
 * @version $Revision: 1.1 $ $Date: 2008/04/30 14:17:44 $
 */

public interface GenericDAO<T extends PersistentObject> {

	public T findById(Long id);

	public List<T> findAll();

	public T save(T entity);

	public void delete(T entity);

	public void delete(Long id);

}
