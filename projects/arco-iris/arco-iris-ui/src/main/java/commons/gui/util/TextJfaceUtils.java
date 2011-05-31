package commons.gui.util;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

public abstract class TextJfaceUtils {

	public static void copyToClipboard(String textToCopy) {
		Clipboard clipboard = new Clipboard(PageHelper.getMainShell().getDisplay());
		String plainText = textToCopy;
		String rtfText = textToCopy;
		TextTransfer textTransfer = TextTransfer.getInstance();
		RTFTransfer rftTransfer = RTFTransfer.getInstance();

		clipboard.setContents(new String[] { plainText, rtfText }, new Transfer[] { textTransfer, rftTransfer });
		clipboard.dispose();
	}

}
