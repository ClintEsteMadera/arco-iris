package commons.gui.widget;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.custom.CTabItem;

import commons.auth.AuthorizationHelper;
import commons.context.BaseApplicationContext;
import commons.context.ScenariosUIApplicationContext;
import commons.gui.action.ScenariosUIActions;
import commons.gui.menu.ScenarioMenuCreator;
import commons.gui.widget.composite.QueryComposite;
import commons.properties.ScenariosUILabels;
import commons.properties.TableConstants;

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
		new ScenarioMenuCreator().createMenu(menuManager);
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

	private void registerScenariosUIEnumProperties() {
		super.enumPropertyDirectory.register(ScenariosUILabels.class);
	}

	private void registerScenariosUIGuiActions() {
		final AuthorizationHelper authHelper = this.getAuthorizationHelper();

		ScenariosUIActions.initialize(authHelper);
	}

	/**
	 * Actualiza la tabla con los �tems de una consulta determinada, teniendo en consideraci�n que
	 * si un �tem es modificado, s�lo se pide a la capa de persistencia dichos �tems y no todos;
	 * favoreciendo la performance del sistema.
	 *
	 * @param nombreConsulta
	 *            el nombre de la consulta. NO puede ser nulo.
	 * @param id
	 *            el identificador �nico del objeto que se intenta refrescar. (si el par�metro es
	 *            nulo, la consulta se actualizar� completa.
	 */
	public void resetConsulta(TableConstants nombreConsulta, Long id) {
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
	 * M�todo de conveniencia para realizar invocaciones m�s limpias (desde la perspectiva del
	 * usuario). Es equivalente a invocar al m�todo resetConsulta(TableConstants, Long) con el
	 * segundo par�metro en <code>null</code>.
	 *
	 * @param nombreConsulta
	 *            el nombre de la consulta. NO puede ser nulo.
	 */
	public void resetConsulta(TableConstants nombreConsulta) {
		this.resetConsulta(nombreConsulta, null);
	}

}
