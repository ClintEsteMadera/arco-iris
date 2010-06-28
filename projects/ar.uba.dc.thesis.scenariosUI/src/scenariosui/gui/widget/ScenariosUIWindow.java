package scenariosui.gui.widget;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.custom.CTabItem;

import scenariosui.context.ScenariosUIApplicationContext;
import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.menu.SelfHealingConfigurationMenuCreator;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.TableConstants;

import commons.auth.AuthorizationHelper;
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

	public static synchronized ScenariosUIWindow getInstance() {
		return instance;
	}

	@Override
	protected void agregarMenuesEspecificos(MenuManager menuManager) {
		// nothing for now
	}

	@Override
	protected void addSpecificActionsToFileMenu(MenuManager fileMenu) {
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
		super.enumPropertyDirectory.register(TableConstants.class, ScenariosUILabels.class);
	}

	private void registerScenariosUIGuiActions() {
		final AuthorizationHelper authHelper = this.getAuthorizationHelper();

		ScenariosUIActions.initialize(authHelper);
	}

	/**
	 * Actualiza la tabla con los �tems de una consulta determinada, teniendo en consideraci�n que si un �tem es
	 * modificado, s�lo se pide a la capa de persistencia dichos �tems y no todos; favoreciendo la performance del
	 * sistema.
	 * 
	 * @param nombreConsulta
	 *            el nombre de la consulta. NO puede ser nulo.
	 * @param id
	 *            el identificador �nico del objeto que se intenta refrescar. (si el par�metro es nulo, la consulta se
	 *            actualizar� completa.
	 */
	public void resetQuery(TableConstants nombreConsulta, Long id) {
		CTabItem tabItem = super.getTabItem(nombreConsulta);
		if (tabItem != null) {
			QueryComposite queryComposite = (QueryComposite) tabItem.getControl();
			if (id == null) {
				queryComposite.reset();
			} else {
				queryComposite.reset(id);
			}
		}
	}

	/**
	 * M�todo de conveniencia para realizar invocaciones m�s limpias (desde la perspectiva del usuario). Es equivalente
	 * a invocar al m�todo resetConsulta(TableConstants, Long) con el segundo par�metro en <code>null</code>.
	 * 
	 * @param nombreConsulta
	 *            el nombre de la consulta. NO puede ser nulo.
	 */
	public void resetConsulta(TableConstants nombreConsulta) {
		this.resetQuery(nombreConsulta, null);
	}
}
