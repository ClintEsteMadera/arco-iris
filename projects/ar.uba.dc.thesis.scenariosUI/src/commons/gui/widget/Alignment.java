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
 * $Id: Alignment.java,v 1.1 2008/01/31 19:44:43 cvschioc Exp $
 */

package commons.gui.widget;

import org.eclipse.swt.SWT;

/**
 * Wrapper del estilo de alineación de SWT. (izquierda, derecha, centrado)
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/01/31 19:44:43 $
 */

public enum Alignment {
	LEFT(SWT.LEFT),
	RIGHT(SWT.RIGHT),
	CENTER(SWT.CENTER);

	private Alignment(int SWTAlignmentStyle) {
		this.SWTAlignmentStyle = SWTAlignmentStyle;
	}

	public int getSWTAlignmentStyle() {
		return this.SWTAlignmentStyle;
	}

	public static Alignment getAlignmentFrom(int SWTAlignmentStyle) {
		Alignment result = null;
		switch (SWTAlignmentStyle) {
		case SWT.LEFT:
			result = Alignment.LEFT;
			break;
		case SWT.RIGHT:
			result = Alignment.RIGHT;
			break;
		case SWT.CENTER:
			result = Alignment.CENTER;
			break;
		default:
			throw new IllegalArgumentException("El estilo " + SWTAlignmentStyle + " no es válido.");
		}
		return result;
	}

	private int SWTAlignmentStyle;
}