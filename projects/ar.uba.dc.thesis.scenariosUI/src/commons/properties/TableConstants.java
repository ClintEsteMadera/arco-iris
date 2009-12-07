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