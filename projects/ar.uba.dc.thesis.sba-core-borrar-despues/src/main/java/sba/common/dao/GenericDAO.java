/*
 * Licencia de Caja de Valores S.A., Versión 1.0
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
