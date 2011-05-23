package commons.pref;

import commons.pref.domain.Preferences;
import commons.pref.domain.TableInfo;
import commons.properties.EnumProperty;

/**
 * This class is in charge of loading and storing user preferences.
 */
public class PreferencesManager {

	private Preferences preferences;

	private static PreferencesManager instance;

	public static PreferencesManager getInstance() {
		if (instance == null) {
			instance = new PreferencesManager();
		}
		return instance;
	}

	/**
	 * Creates an instance of PreferencesManager. This object merges any pre-existing preferences with the default ones
	 * (this is particularly useful when new info is added)
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

	public TableInfo getTableInfo(EnumProperty tableName) {
		return this.preferences.getTableInfo(tableName);
	}

	public void persistPreferences() {
		PreferencesHelper.persistPreferences(this.preferences);
	}
}