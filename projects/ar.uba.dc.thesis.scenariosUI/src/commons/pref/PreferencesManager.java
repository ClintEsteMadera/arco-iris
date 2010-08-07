package commons.pref;

import commons.pref.domain.Preferences;
import commons.pref.domain.TableInfo;
import commons.properties.EnumProperty;

/**
 * Clase que Maneja el almacenamiento y carga de las preferencias visuales del Sistema.
 * 
 */
public class PreferencesManager {

	public static PreferencesManager getInstance() {
		if (instance == null) {
			instance = new PreferencesManager();
		}
		return instance;
	}

	public TableInfo getTableInfo(EnumProperty tableName) {
		return this.preferences.getTableInfo(tableName);
	}

	public void persistPreferences() {
		PreferencesHelper.persistPreferences(this.preferences);
	}

	/**
	 * Crea un manejador de preferencias, mergeando la información del archivo de preferencias del usuario con el
	 * default, en caso que el mismo exista.
	 */
	private PreferencesManager() {
		Preferences defaultPrefs = PreferencesHelper.getDefaultUserPreferences();
		Preferences userCustomPrefs = PreferencesHelper.getCustomizedUserPreferences();
		if (userCustomPrefs == null) {
			this.preferences = defaultPrefs;
		} else {
			this.preferences = PreferencesHelper.merge(userCustomPrefs, defaultPrefs);
		}
	}

	private Preferences preferences;

	private static PreferencesManager instance;
}