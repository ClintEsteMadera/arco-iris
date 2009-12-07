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
 * $Id: TableConstants.java,v 1.29 2008/04/30 17:31:40 cvscalab Exp $
 */

package commons.properties;

import java.util.Properties;

import sba.common.properties.EnumPropertiesHelper;
import sba.common.properties.EnumProperty;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.29 $ $Date: 2008/04/30 17:31:40 $
 */

public enum TableConstants implements EnumProperty {

	SCENARIOS_QUERY;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(props, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(props, this.name(), reemplazos);
	}

	private static Properties props = EnumPropertiesHelper.load("/tableconstants.properties");
}