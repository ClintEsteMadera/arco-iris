package ar.uba.dc.arcoirisui.gui.widget;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;

import ar.uba.dc.arcoirisui.gui.action.ArcoIrisUIActions;
import ar.uba.dc.arcoirisui.gui.menu.SelfHealingConfigurationMenuCreator;
import ar.uba.dc.arcoirisui.gui.thread.ArcoIrisUIUncaughtExceptionHandler;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;
import ar.uba.dc.arcoirisui.properties.UniqueTableIdentifier;
import ar.uba.dc.arcoirisui.service.ArcoIrisUISystemConfiguration;

import commons.auth.AuthorizationManager;
import commons.core.BaseSystemConfiguration;
import commons.gui.widget.MainWindow;
import commons.gui.widget.composite.QueryComposite;

/**
 * Entry Point for "Arco Iris UI" application.
 */
public class ArcoIrisUIMainWindow extends MainWindow {

	private static final ArcoIrisUIUncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER = new ArcoIrisUIUncaughtExceptionHandler();

	private static ArcoIrisUIMainWindow instance = new ArcoIrisUIMainWindow();

	public static void main(String[] args) {
		getInstance().run();
	}

	private ArcoIrisUIMainWindow() {
		super();
	}

	public static synchronized ArcoIrisUIMainWindow getInstance() {
		return instance;
	}

	@Override
	protected void addSpecificMenus(MenuManager menuManager) {
		// nothing for now
	}

	@Override
	protected void addSpecificItemsToFileMenu(MenuManager fileMenu) {
		new SelfHealingConfigurationMenuCreator().createMenu(fileMenu);
	}

	@Override
	public BaseSystemConfiguration getSystemConfiguration() {
		return ArcoIrisUISystemConfiguration.getInstance();
	}

	@Override
	protected void initialize() {
		this.registerArcoIrisUIEnumProperties();
		this.registerArcoIrisUIGuiActions();
	}

	@SuppressWarnings("unchecked")
	private void registerArcoIrisUIEnumProperties() {
		super.enumPropertyDirectory.register(UniqueTableIdentifier.class, ArcoIrisUILabels.class);
	}

	private void registerArcoIrisUIGuiActions() {
		final AuthorizationManager authHelper = this.getAuthorizationManager();

		ArcoIrisUIActions.initialize(authHelper);
	}

	/**
	 * Actualiza la(s) tabla(s) con los ítems de una consulta determinada, teniendo en consideración que si un ítem es
	 * modificado, sólo se pide a la capa de persistencia dichos ítems y no todos; favoreciendo la performance del
	 * sistema.
	 * 
	 * @param id
	 *            el identificador único del objeto que se intenta refrescar. (si el parámetro es nulo, la consulta se
	 *            actualizará completa.
	 * @param queryNames
	 *            el nombre de la consulta. NO puede ser nulo.
	 */
	public void resetQuery(Long id, UniqueTableIdentifier... queryNames) {
		List<CTabItem> tabItems = super.getTabItems(queryNames);
		for (CTabItem tabItem : tabItems) {
			if (tabItem != null) {
				@SuppressWarnings("rawtypes")
				QueryComposite queryComposite = (QueryComposite) tabItem.getControl();
				if (id == null) {
					queryComposite.reset();
				} else {
					queryComposite.reset(id);
				}
			}
		}
	}

	/**
	 * Método de conveniencia para realizar invocaciones más limpias (desde la perspectiva del usuario). Es equivalente
	 * a invocar al método resetQuery(Long, UniqueTableIdentifier) con el primer parámetro en <code>null</code>.
	 * 
	 * @param queryName
	 *            el nombre de la consulta. NO puede ser nulo.
	 */
	public void resetQuery(UniqueTableIdentifier queryName) {
		this.resetQuery(null, queryName);
	}

	@Override
	public UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
		return UNCAUGHT_EXCEPTION_HANDLER;
	}

	@Override
	protected Image[] getIcons() {
		return new Image[] {
				ImageDescriptor.createFromFile(MainWindow.class, "/images/arco_iris_16x16.png").createImage(),
				ImageDescriptor.createFromFile(MainWindow.class, "/images/arco_iris_32x32.png").createImage(),
				ImageDescriptor.createFromFile(MainWindow.class, "/images/arco_iris_48x48.png").createImage() };
	}
}
