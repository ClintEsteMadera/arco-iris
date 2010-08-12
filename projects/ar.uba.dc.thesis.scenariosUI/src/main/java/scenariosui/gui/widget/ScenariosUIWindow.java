package scenariosui.gui.widget;

import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.custom.CTabItem;

import scenariosui.context.ScenariosUIApplicationContext;
import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.menu.SelfHealingConfigurationMenuCreator;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.UniqueTableIdentifier;

import commons.auth.AuthorizationManager;
import commons.context.BaseApplicationContext;
import commons.gui.widget.MainWindow;
import commons.gui.widget.composite.QueryComposite;

/**
 * Entry Point for the "Scenarios UI" application
 * 
 */
public class ScenariosUIWindow extends MainWindow {

	private static ScenariosUIWindow instance = new ScenariosUIWindow();

	public static void main(String[] args) {
		getInstance().run();
	}

	private ScenariosUIWindow() {
		super();
	}

	public static synchronized ScenariosUIWindow getInstance() {
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
	public BaseApplicationContext getApplicationContext() {
		return ScenariosUIApplicationContext.getInstance();
	}

	@Override
	protected void initialize() {
		this.registerScenariosUIEnumProperties();
		this.registerScenariosUIGuiActions();
	}

	@SuppressWarnings("unchecked")
	private void registerScenariosUIEnumProperties() {
		super.enumPropertyDirectory.register(UniqueTableIdentifier.class, ScenariosUILabels.class);
	}

	private void registerScenariosUIGuiActions() {
		final AuthorizationManager authHelper = this.getAuthorizationManager();

		ScenariosUIActions.initialize(authHelper);
	}

	/**
	 * Actualiza la(s) tabla(s) con los �tems de una consulta determinada, teniendo en consideraci�n que si un �tem es
	 * modificado, s�lo se pide a la capa de persistencia dichos �tems y no todos; favoreciendo la performance del
	 * sistema.
	 * 
	 * @param id
	 *            el identificador �nico del objeto que se intenta refrescar. (si el par�metro es nulo, la consulta se
	 *            actualizar� completa.
	 * @param queryNames
	 *            el nombre de la consulta. NO puede ser nulo.
	 */
	@SuppressWarnings("unchecked")
	public void resetQuery(Long id, UniqueTableIdentifier... queryNames) {
		List<CTabItem> tabItems = super.getTabItems(queryNames);
		for (CTabItem tabItem : tabItems) {
			if (tabItem != null) {
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
	 * M�todo de conveniencia para realizar invocaciones m�s limpias (desde la perspectiva del usuario). Es equivalente
	 * a invocar al m�todo resetQuery(Long, UniqueTableIdentifier) con el primer par�metro en <code>null</code>.
	 * 
	 * @param queryName
	 *            el nombre de la consulta. NO puede ser nulo.
	 */
	public void resetQuery(UniqueTableIdentifier queryName) {
		this.resetQuery(null, queryName);
	}
}
