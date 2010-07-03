package commons.gui.widget;

import java.lang.Thread.UncaughtExceptionHandler;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import sba.common.session.SessionHelper;

import commons.auth.AuthenticationHelper;
import commons.auth.AuthorizationHelper;
import commons.auth.DummyAuthenticationHelper;
import commons.auth.DummyAuthorizationHelper;
import commons.context.BaseApplicationContext;
import commons.core.BaseSystemConfiguration;
import commons.gui.action.AboutAction;
import commons.gui.action.ChangePwdAction;
import commons.gui.action.ExitAction;
import commons.gui.action.HelpAction;
import commons.gui.thread.GUIUncaughtExceptionHandler;
import commons.gui.util.PageHelper;
import commons.gui.widget.dialog.LoginDialog;
import commons.pref.PreferencesManager;
import commons.properties.CommonConstants;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;
import commons.properties.EnumPropertyDirectory;

public abstract class MainWindow extends ApplicationWindow {

	public void run() {
		// Don't return from open() until window closes
		setBlockOnOpen(true);
		// Open the main window
		open();
		// Dispose the display
		Display.getCurrent().dispose();
	}

	public void setDefaultStatusMessage() {
		super.setStatus(getDefaultStatusMessage());
	}

	public String getDefaultStatusMessage() {
		return CommonConstants.APP_NAME.toString();
	}

	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager();
		addFileMenu(menuManager);
		addSpecificMenus(menuManager);
		addHelpMenu(menuManager);

		return menuManager;
	}

	private void addFileMenu(MenuManager menuManager) {
		String menuId = CommonLabels.MENU_FILE.toString();
		MenuManager fileMenu = new MenuManager(menuId, menuId);
		menuManager.add(fileMenu);
		addSpecificItemsToFileMenu(fileMenu);
		fileMenu.add(new ChangePwdAction(getAuthenticationHelper()));
		fileMenu.add(new ExitAction());
	}

	private void addHelpMenu(MenuManager menuManager) {
		String menuId = CommonLabels.MENU_HELP.toString();
		MenuManager helpMenu = new MenuManager(menuId, menuId);
		menuManager.add(helpMenu);
		helpMenu.add(new HelpAction());
		helpMenu.add(SEPARATOR);
		helpMenu.add(new AboutAction());
	}

	/**
	 * Este método agrega los menúes especifícos de cada aplicación.
	 * 
	 * @param menuManager
	 *            el objeto menuManager sobre el cual crear los menúes.
	 */
	protected abstract void addSpecificMenus(MenuManager menuManager);

	/**
	 * This method allow the concrete application to add one or more actions within the "File" menu.
	 * 
	 * @param fileMenu
	 *            the menu where to add action(s)
	 */
	protected abstract void addSpecificItemsToFileMenu(MenuManager fileMenu);

	public CTabItem getTabItem(EnumProperty tabItemText) {
		CTabItem tabItem = null;
		if (mainTabFolder.getItems() != null) {
			for (CTabItem item : mainTabFolder.getItems()) {
				if (item.getText().equals(tabItemText.toString())) {
					tabItem = item;
				}
			}
		}
		return tabItem;
	}

	public IAction getMenuItem(String itemId, EnumProperty... menuNames) {
		StringBuilder sb = new StringBuilder();
		for (EnumProperty menuName : menuNames) {
			sb.append(menuName.toString()).append("/");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1); // delete the extra slash
		}
		IMenuManager specificMenu = this.getMenuBarManager().findMenuUsingPath(sb.toString());

		return ((ActionContributionItem) specificMenu.find(itemId)).getAction();
	}

	@Override
	protected Control createContents(Composite parent) {
		mainTabFolder = new CTabFolder(parent, SWT.TOP);
		mainTabFolder.setBorderVisible(true);
		mainTabFolder.setVisible(false);
		CTabFolder2Listener listener = new CTabFolder2Adapter() {
			@Override
			public void close(CTabFolderEvent event) {
				if (mainTabFolder.getItemCount() == 1) {
					mainTabFolder.setVisible(false);
				}
			}
		};
		mainTabFolder.addCTabFolder2Listener(listener);
		this.setDefaultStatusMessage();
		return parent;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setMaximized(true);
		String usuarioConectado = SessionHelper.nombreDeUsuarioConectado();
		if (usuarioConectado == null) {
			usuarioConectado = "Unknown User";
		}
		shell.setText(getDefaultStatusMessage() + " [" + usuarioConectado + "]");
		shell.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_GRAY));
		PageHelper.setMainShell(shell);
	}

	@Override
	protected void handleShellCloseEvent() {
		try {
			PreferencesManager.getInstance().persistPreferences();
		} catch (Exception e) {
			log.fatal(e.getMessage());
		} finally {
			System.exit(0);
		}
	}

	@Override
	protected boolean canHandleShellCloseEvent() {
		return super.canHandleShellCloseEvent() && ExitAction.confirmExit();
	}

	protected UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
		return GUIUncaughtExceptionHandler.getInstance();
	}

	protected MainWindow() {
		super(null);
		PageHelper.setMainWindow(this);
		setShellStyle(SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.CLOSE);
		setDefaultImages(getImages());
		setUpExceptionHandlers();
		this.enumPropertyDirectory = new EnumPropertyDirectory();
		doLogin();
		initialize();
		addMenuBar();
		addStatusLine();
		initializeUserPreferences();
	}

	private void setUpExceptionHandlers() {
		final UncaughtExceptionHandler uncaughtExceptionHandler = getDefaultUncaughtExceptionHandler();
		// SWT Exception Handler
		Window.setExceptionHandler(new IExceptionHandler() {
			public void handleException(Throwable t) {
				uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), t);
			}
		});

		// Java Exception Handler
		Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
	}

	private void initializeUserPreferences() {
		PreferencesManager.getInstance();
	}

	private void doLogin() {
		BaseSystemConfiguration config = this.getSystemConfiguration();
		AuthenticationHelper authHelper = this.getAuthenticationHelper();
		if (config.ambienteDesarrollo()) {
			SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
			authHelper.authenticate(config.usuarioDesarrollo(), config.passwordDesarrollo());
		} else {
			LoginDialog loginDialog = new LoginDialog(getShell(), authHelper);
			if (loginDialog.open() == Window.CANCEL) {
				System.exit(0);
			}
		}
	}

	protected Image[] getImages() {
		return new Image[] { ImageDescriptor.createFromFile(MainWindow.class, "/images/16x16.png").createImage(),
				ImageDescriptor.createFromFile(MainWindow.class, "/images/32x32.png").createImage(),
				ImageDescriptor.createFromFile(MainWindow.class, "/images/48x48.png").createImage() };
	}

	private AuthenticationHelper getAuthenticationHelper() {
		return AUTHENTICATION_HELPER;
	}

	public AuthorizationHelper getAuthorizationHelper() {
		return AUTHORIZATION_HELPER;
	}

	public BaseSystemConfiguration getSystemConfiguration() {
		return this.getApplicationContext().getSystemConfiguration();
	}

	public EnumPropertyDirectory getEnumPropertyDirectory() {
		return enumPropertyDirectory;
	}

	/**
	 * Aquí se provee una instancia concreta del ApplicationContext particular de la aplicación.
	 */
	public abstract BaseApplicationContext getApplicationContext();

	/**
	 * En este punto se debería inicializarse todo lo que sea posible/necesario.
	 */
	protected abstract void initialize();

	public PreferencePage currentPreferencePage;

	public CTabFolder mainTabFolder;

	protected EnumPropertyDirectory enumPropertyDirectory;

	protected static final Separator SEPARATOR = new Separator();

	private static final AuthenticationHelper AUTHENTICATION_HELPER = new DummyAuthenticationHelper();

	private static final AuthorizationHelper AUTHORIZATION_HELPER = new DummyAuthorizationHelper();

	private static final Log log = LogFactory.getLog(MainWindow.class);
}