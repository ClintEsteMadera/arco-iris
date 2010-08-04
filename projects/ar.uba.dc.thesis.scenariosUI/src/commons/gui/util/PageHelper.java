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
 * $Id: PageHelper.java,v 1.7 2008/04/22 19:54:54 cvschioc Exp $
 */

package commons.gui.util;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import commons.gui.widget.MainWindow;

public abstract class PageHelper {

	public static Rectangle getCenterLocation(int width, int height) {
		Rectangle screenSize = Display.getDefault().getBounds();
		int xCoord = (screenSize.width - width) / 2;
		int yCoord = (screenSize.height - height) / 2;
		return new Rectangle(xCoord, yCoord, width, height);
	}

	/**
	 * Retorna la posici�n considerando el ancho y largo relativo al tama�o de la pantalla
	 * 
	 * @param widthProp
	 * @param heightProp
	 * @return
	 */
	public static Rectangle getRelativeCenterLocation(double widthProp, double heightProp) {
		Rectangle screenSize = Display.getDefault().getBounds();
		int width = (int) (screenSize.width * widthProp);
		int height = (int) (screenSize.height * heightProp);
		return getCenterLocation(width, height);
	}

	public static void setRelativeCenterLocation(Shell shell) {
		Point size = shell.getSize();
		Rectangle centerLocation = PageHelper.getRelativeCenterLocation(size.y, size.x);
		shell.setBounds(centerLocation);
	}

	public static void setCenterLocation(Shell shell) {
		Point size = shell.getSize();
		PageHelper.setCenterLocation(shell, size.y, size.x);
	}

	public static void setCenterLocation(Shell shell, int width) {
		Point size = shell.getSize();
		PageHelper.setCenterLocation(shell, width, size.x);
	}

	public static void setCenterLocation(Shell shell, int width, int heigth) {
		Rectangle centerLocation = PageHelper.getCenterLocation(width, heigth);
		shell.setBounds(centerLocation);
	}

	public static int getCantidadDePixels(int cantChars) {
		return cantChars * getPixelsPerChar();
	}

	public static int getPixelsPerChar() {
		return fontMetrics.getAverageCharWidth() + 1;
	}

	public static int getMinimunCharHeight() {
		return fontMetrics.getHeight();
	}

	/**
	 * Returns the number of pixels corresponding to the given number of horizontal dialog units.
	 * 
	 * @param dialogUnits
	 *            the number of horizontal dialog units
	 * @return the number of pixels
	 */
	public static int convertHorizontalDLUsToPixels(int dialogUnits) {
		if (fontMetrics == null) {
			return 0;
		}
		return Dialog.convertHorizontalDLUsToPixels(fontMetrics, dialogUnits);
	}

	/**
	 * Guarda la referencia a la ventana principal para que otros objetos puedan enviarle mensajes.
	 * 
	 * @param mainWindow
	 *            la instancia concreta de MainWindow.
	 */
	public static void setMainWindow(MainWindow mainWindow) {
		mainWindowInstance = mainWindow;
	}

	/**
	 * Obtiene la ventana principal de la aplicaci�n, si� la misma se encuentra seteada, caso contrario arroja una
	 * RuntimeException.
	 */
	public static MainWindow getMainWindow() {
		if (mainWindowInstance == null) {
			throw new RuntimeException("La ventana principal no ha sido seteada");
		}
		return mainWindowInstance;
	}

	/**
	 * Recibe el shell de la ventana Principal y cachea el display
	 * 
	 * @see MainWindow
	 * @param mainShell
	 *            shell de la ventana Principal
	 * @return
	 */
	public static void setMainShell(Shell mainShell) {
		shell = mainShell;
		GC gc = new GC(shell);
		gc.setFont(JFaceResources.getDialogFont());
		fontMetrics = gc.getFontMetrics();
	}

	/**
	 * Obtiene el shell principal.
	 */
	public static Shell getMainShell() {
		return shell;
	}

	public static Display getDisplay() {
		return Display.getDefault();
	}

	public static Color getValidColor() {
		return getDisplay().getSystemColor(SWT.COLOR_BLACK);
	}

	public static Color getInvalidColor() {
		return getDisplay().getSystemColor(SWT.COLOR_RED);
	}

	public static Color getNotEditableTextColor() {
		return getDisplay().getActiveShell().getBackground();
	}

	public static Font getValueLabelsFont() {
		if (valueLabelsFont == null) {
			float height = getDisplay().getSystemFont().getFontData()[0].height;
			valueLabelsFont = new Font(getDisplay(), "", (int) height, SWT.BOLD);
		}
		return valueLabelsFont;
	}

	private static MainWindow mainWindowInstance;

	private static Shell shell;

	private static FontMetrics fontMetrics;

	private static Font valueLabelsFont;
}