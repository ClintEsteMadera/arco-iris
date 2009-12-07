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
 * $Id: ContactoDAOHibernate.java,v 1.1 2007/10/02 20:56:01 cvschioc Exp $
 */

package sba.common.contacto.core.dao.hibernate;

import sba.common.contacto.Contacto;
import sba.common.dao.HibernateCrudDAO;

/**
 * DAO de Hibernate para <code>Contacto</code>
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2007/10/02 20:56:01 $
 */

public class ContactoDAOHibernate extends HibernateCrudDAO<Contacto> {

	@Override
	protected Class<Contacto> getObjectClass() {
		return Contacto.class;
	}

}
