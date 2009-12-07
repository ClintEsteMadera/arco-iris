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
 * $Id: TextJfaceUtils.java,v 1.2 2007/08/09 20:27:30 cvschioc Exp $
 */
package commons.gui.util;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

/**
 *
 * @author Gabriel Tursi
 * @version $Revision: 1.2 $ $Date: 2007/08/09 20:27:30 $
 */
public abstract class TextJfaceUtils {

	public static void copyToClipboard(String textToCopy) {
		Clipboard clipboard = new Clipboard(PageHelper.getMainShell().getDisplay());
		String plainText = textToCopy;
		String rtfText = textToCopy;
		TextTransfer textTransfer = TextTransfer.getInstance();
		RTFTransfer rftTransfer = RTFTransfer.getInstance();

		clipboard.setContents(new String[] { plainText, rtfText }, new Transfer[] { textTransfer,
				rftTransfer });
		clipboard.dispose();
	}
	
}

