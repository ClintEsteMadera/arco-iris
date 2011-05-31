package commons.gui.widget;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

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

import commons.auth.AuthenticationManager;
import commons.auth.AuthorizationManager;
import commons.auth.DummyAuthenticationManager;
import commons.auth.DummyAuthorizationManager;
import commons.core.BaseSystemConfiguration;
import commons.gui.action.AboutAction;
import commons.gui.action.ChangePwdAction;
import commons.gui.action.ExitAction;
import commons.gui.thread.DefaultGUIUncaughtExceptionHandler;
import commons.gui.util.PageHelper;
import commons.gui.widget.dialog.LoginDialog;
import commons.pref.PreferencesManager;
import commons.properties.CommonConstants;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;
import commons.properties.EnumPropertyDirectory;
import commons.session.SessionHelper;

public abstract class MainWindow extends ApplicationWindow {

	public PreferencePage currentPreferencePage;

	public CTabFolder mainTabFolder;

	protected EnumPropertyDirectory enumPropertyDirectory;

	protected static final Separator SEPARATOR = new Separator();

	private static final AuthenticationManager AUTHENTICATION_MANAGER = new DummyAuthenticationManager();

	private static final AuthorizationManager AUTHORIZATION_HELPER = new DummyAuthorizationManager();

	private static final UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER = new DefaultGUIUncaughtExceptionHandler();

	private static final Log log = LogFactory.getLog(MainWindow.class);

	protected MainWindow() {
		super(null);
		PageHelper.setMainWindow(this);
		setShellStyle(SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.CLOSE);
		setDefaultImages(getIcons());
		setUpExceptionHandlers();
		this.enumPropertyDirectory = new EnumPropertyDirectory();
		doLogin();
		initialize();
		addMenuBar();
		addStatusLine();
		initializeUserPreferences();
	}

	public void run() {
		// Don't return from open() until window closes
		setBlockOnOpen(true);
		// Open the main window
		open();
		// Dispose the display
		Display.getCurrent().dispose();
	}

	public String getDefaultStatusMessage() {
		return CommonConstants.APP_NAME.toString();
	}

	public void setDefaultStatusMessage() {
		super.setStatus(getDefaultStatusMessage());
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
		fileMenu.add(new ChangePwdAction(getAuthenticationManager()));
		fileMenu.add(new ExitAction());
	}

	private void addHelpMenu(MenuManager menuManager) {
		String menuId = CommonLabels.MENU_HELP.toString();
		MenuManager helpMenu = new MenuManager(menuId, menuId);
		menuManager.add(helpMenu);
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
	 * This method allow the concrete application to add one or more actions
	 * within the "File" menu.
	 * 
	 * @param fileMenu
	 *            the menu where to add action(s)
	 */
	protected abstract void addSpecificItemsToFileMenu(MenuManager fileMenu);

	public List<CTabItem> getTabItems(EnumProperty... tabItemTexts) {
		List<CTabItem> tabItems = new ArrayList<CTabItem>();
		if (mainTabFolder.getItems() != null) {
			for (CTabItem item : mainTabFolder.getItems()) {
				for (EnumProperty tabItemText : tabItemTexts) {
					if (tabItemText.toString().equals(item.getText())) {
						tabItems.add(item);
						break;
					}
				}
			}
		}
		return tabItems;
	}

	public CTabItem getTabItem(EnumProperty tabItemText) {
		CTabItem tabItem = null;
		if (mainTabFolder.getItems() != null) {
			for (CTabItem item : mainTabFolder.getItems()) {
				if (item.getText().equals(tabItemText.toString())) {
					tabItem = item;
					break;
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
		IMenuManager specificMenu = this.getMenuBarManager().findMenuUsingPath(
				sb.toString());

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
		String userLoggedIn = SessionHelper.usernameOfTheConnectedUser();
		if (userLoggedIn == null) {
			userLoggedIn = "Unknown User";
		}
		shell.setText(getDefaultStatusMessage() + " [" + userLoggedIn + "]");
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

	public UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
		return UNCAUGHT_EXCEPTION_HANDLER;
	}

	private void setUpExceptionHandlers() {
		final UncaughtExceptionHandler uncaughtExceptionHandler = this
				.getDefaultUncaughtExceptionHandler();

		// SWT Exception Handler
		Window.setExceptionHandler(new IExceptionHandler() {
			public void handleException(Throwable t) {
				uncaughtExceptionHandler.uncaughtException(
						Thread.currentThread(), t);
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
		AuthenticationManager authManager = this.getAuthenticationManager();
		if (config.developmentEnvironment()) {
			SecurityContextHolder
					.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
			authManager.authenticate(config.developmentUser(),
					config.developmentPassword());
		} else {
			LoginDialog loginDialog = new LoginDialog(getShell(), authManager);
			if (loginDialog.open() == Window.CANCEL) {
				System.exit(0);
			}
		}
	}

	/**
	 * Gets the array of images to be used when this window is opened. It is
	 * expected that the array will contain the same icon rendered at different
	 * resolutions.
	 * 
	 * @see org.eclipse.swt.widgets.Decorations#setImages(org.eclipse.swt.graphics.Image[])
	 */
	protected Image[] getIcons() {
		return new Image[] {
				ImageDescriptor.createFromFile(MainWindow.class,
						"/images/16x16.png").createImage(),
				ImageDescriptor.createFromFile(MainWindow.class,
						"/images/32x32.png").createImage(),
				ImageDescriptor.createFromFile(MainWindow.class,
						"/images/48x48.png").createImage() };
	}

	private AuthenticationManager getAuthenticationManager() {
		return AUTHENTICATION_MANAGER;
	}

	public AuthorizationManager getAuthorizationManager() {
		return AUTHORIZATION_HELPER;
	}

	public EnumPropertyDirectory getEnumPropertyDirectory() {
		return enumPropertyDirectory;
	}

	/**
	 * Get an instance of the system configuration.
	 */
	public abstract BaseSystemConfiguration getSystemConfiguration();

	/**
	 * At this point, the users can initialize anything they need.
	 */
	protected abstract void initialize();
}