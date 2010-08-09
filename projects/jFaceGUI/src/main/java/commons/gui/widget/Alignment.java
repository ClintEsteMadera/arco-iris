package commons.gui.widget;

import org.eclipse.swt.SWT;

/**
 * Wrapper del estilo de alineación de SWT. (izquierda, derecha, centrado)
 * 
 */

public enum Alignment {
	LEFT(SWT.LEFT), RIGHT(SWT.RIGHT), CENTER(SWT.CENTER);

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