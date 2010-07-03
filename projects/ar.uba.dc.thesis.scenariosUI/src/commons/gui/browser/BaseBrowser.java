package commons.gui.browser;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;


import commons.gui.util.PageHelper;
import commons.properties.CommonConstants;
import commons.properties.CommonLabels;

/**
 * Base Browser
 * @author Gabriel Tursi
 * @version $Revision: 1.5 $ $Date: 2007/11/30 20:31:06 $
 */
public class BaseBrowser {

	public BaseBrowser() {
		shell = new Shell(PageHelper.getMainShell());
		shell.setLayout(new GridLayout());
		shell.setMaximized(true);
		shell.setText(getShellText());

		ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		final ToolBarManager manager = new ToolBarManager(toolBar);

		Composite compositeLocation = new Composite(shell, SWT.NULL);
		compositeLocation.setLayout(new GridLayout(3, false));
		compositeLocation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		browser = new Browser(shell, SWT.BORDER);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		browser.setUrl(getURL());
		Composite compositeStatus = new Composite(shell, SWT.NULL);
		compositeStatus.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		compositeStatus.setLayout(new GridLayout(2, false));

		labelStatus = new Label(compositeStatus, SWT.NULL);
		labelStatus.setText("Ready");
		labelStatus.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final ProgressBar progressBar = new ProgressBar(compositeStatus, SWT.SMOOTH);

		// Adds tool bar items using actions.
		final Action actionBackward = new Action("&Backward", ImageDescriptor.createFromFile(
				BaseBrowser.class, "/images/browser/back.gif")) {
			@Override
			public void run() {
				browser.back();
			}
		};
		actionBackward.setEnabled(false); // action is disabled at start up.

		final Action actionForward = new Action("&Forward", ImageDescriptor.createFromFile(
				BaseBrowser.class, "/images/browser/forward.gif")) {
			@Override
			public void run() {
				browser.forward();
			}
		};
		actionForward.setEnabled(false); // action is disabled at start up.

		Action actionStop = new Action("&Stop", ImageDescriptor.createFromFile(BaseBrowser.class,
				"/images/browser/stop.gif")) {
			@Override
			public void run() {
				browser.stop();
			}
		};

		Action actionRefresh = new Action("&Refresh", ImageDescriptor.createFromFile(
				BaseBrowser.class, "/images/browser/refresh.gif")) {
			@Override
			public void run() {
				browser.refresh();
			}
		};

		Action actionHome = new Action("&Home", ImageDescriptor.createFromFile(BaseBrowser.class,
				"/images/browser/home.gif")) {
			@Override
			public void run() {
				browser.setUrl(getURL());
			}
		};

		manager.add(actionBackward);
		manager.add(actionForward);
		manager.add(actionStop);
		manager.add(actionRefresh);
		manager.add(actionHome);

		manager.update(true);
		toolBar.pack();

		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				progressBar.setMaximum(event.total);
				progressBar.setSelection(event.current);
			}

			public void completed(ProgressEvent event) {
				progressBar.setSelection(0);
			}
		});

		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				labelStatus.setText(event.text);
			}
		});

		initialize(shell.getDisplay(), browser);
		shell.open();

		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				// If no more entries in event queue
				shell.getDisplay().sleep();
			}
		}
	}

	protected String getShellText() {
		return CommonLabels.HELP_SHELL_TEXT.toString();
	}

	protected String getURL() {
		return CommonConstants.HELP_URL.toString();
	}

	static void initialize(final Display display, Browser browser) {
		browser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
				Shell shell = new Shell(display);
				shell.setText("New Window");
				shell.setLayout(new FillLayout());
				Browser browser1 = new Browser(shell, SWT.NONE);
				initialize(display, browser1);
				event.browser = browser1;
			}
		});

		browser.addVisibilityWindowListener(new VisibilityWindowListener() {
			public void hide(WindowEvent event) {
				Browser browser1 = (Browser) event.widget;
				Shell shell = browser1.getShell();
				shell.setVisible(false);
			}

			public void show(WindowEvent event) {
				Browser browser1 = (Browser) event.widget;
				Shell shell = browser1.getShell();
				if (event.location != null)
					shell.setLocation(event.location);
				if (event.size != null) {
					Point size = event.size;
					shell.setSize(shell.computeSize(size.x, size.y));
				}
				shell.open();
			}
		});

		browser.addCloseWindowListener(new CloseWindowListener() {
			public void close(WindowEvent event) {
				Browser browser1 = (Browser) event.widget;
				Shell shell = browser1.getShell();
				shell.close();
			}
		});
	}

	public static void show() {
		new BaseBrowser();
	}

	private Shell shell;

	private Browser browser;

	private Label labelStatus;
}