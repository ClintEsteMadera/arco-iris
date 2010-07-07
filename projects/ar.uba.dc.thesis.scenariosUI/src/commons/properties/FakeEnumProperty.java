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