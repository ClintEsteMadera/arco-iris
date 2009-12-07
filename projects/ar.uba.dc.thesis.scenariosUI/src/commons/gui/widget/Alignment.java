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
 * $Id: Alignment.java,v 1.1 2008/01/31 19:44:43 cvschioc Exp $
 */

package commons.gui.widget;

import org.eclipse.swt.SWT;

/**
 * Wrapper del estilo de alineaci�n de SWT. (izquierda, derecha, centrado)
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
			throw new IllegalArgumentException("El estilo " + SWTAlignmentStyle + " no es v�lido.");
		}
		return result;
	}

	private int SWTAlignmentStyle;
}