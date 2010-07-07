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
 * $Id: FakeEnumProperty.java,v 1.4 2008/01/31 17:09:57 cvschioc Exp $
 */

package commons.properties;

/**
 * Fake Enum Property utilazada para wrappear un String.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.4 $ $Date: 2008/01/31 17:09:57 $
 */

public class FakeEnumProperty implements EnumProperty {

	public FakeEnumProperty(String label) {
		super();
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public String toString(Object... reemplazos) {
		return label;
	}

	public String name() {
		return label;
	}

	private String label;

	private static final long serialVersionUID = 1L;
}