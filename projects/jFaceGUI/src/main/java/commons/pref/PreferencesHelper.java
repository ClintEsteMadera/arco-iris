package commons.pref;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import commons.gui.widget.Alignment;
import commons.pref.converter.ColumnInfoConverter;
import commons.pref.converter.TableInfoConverter;
import commons.pref.domain.ColumnInfo;
import commons.pref.domain.Preferences;
import commons.pref.domain.TableInfo;
import commons.properties.CommonConstants;
import commons.utils.FileHelper;

public abstract class PreferencesHelper {

	private static final String USER_PREF_FILE_NAME = FileHelper.OUTPUT_DIR + FileHelper.getFileSeparator()
			+ CommonConstants.PREFERENCES_FILE.toString();

	private static final String DEFAULT_PREF_FILE_NAME = CommonConstants.PREFERENCES_FILE.toString();

	private static XStream xstream = configureXStream();

	private static final Log log = LogFactory.getLog(PreferencesHelper.class);

	static Preferences getDefaultUserPreferences() {
		log.trace("Loading default user preferences from " + DEFAULT_PREF_FILE_NAME);

		InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(DEFAULT_PREF_FILE_NAME);

		if (inputStream == null) {
			String msg = "Default user preferences not found in " + DEFAULT_PREF_FILE_NAME;
			log.fatal(msg);
			throw new RuntimeException(msg);
		}
		return (Preferences) xstream.fromXML(inputStream);
	}

	static Preferences getCustomizedUserPreferences() {
		log.trace("Loading customized user preferences from " + USER_PREF_FILE_NAME);
		Preferences result = null;
		try {
			InputStream inputStream = new FileInputStream(USER_PREF_FILE_NAME);
			result = (Preferences) xstream.fromXML(inputStream);
		} catch (FileNotFoundException ex) {
			String msg = "Customized user preferences not found in " + USER_PREF_FILE_NAME;
			log.info(msg);
		}
		return result;
	}

	static void persistPreferences(Preferences preferences) {
		try {
			xstream.toXML(preferences, new FileOutputStream(USER_PREF_FILE_NAME));
			log.info("User preferences saved in " + USER_PREF_FILE_NAME);
		} catch (FileNotFoundException ex) {
			String msg = "Unable to save user preferences in " + USER_PREF_FILE_NAME;
			log.warn(msg, ex);
		}
	}

	static Preferences merge(Preferences userPrefs, Preferences defaultPrefs) {
		for (TableInfo defaultTableInfo : defaultPrefs.getTables()) {
			TableInfo userTableInfo = userPrefs.getTableInfo(defaultTableInfo.getName());
			if (userTableInfo != null) {
				defaultTableInfo = merge(userTableInfo, defaultTableInfo);
			}
		}
		return defaultPrefs;
	}

	private static TableInfo merge(TableInfo userTableInfo, TableInfo defaultTableInfo) {
		String order = userTableInfo.getOrder();
		if (!StringUtils.isEmpty(order)) {
			defaultTableInfo.setOrder(order);
		}
		ColumnInfo[] columnInfos = merge(userTableInfo.getColumnInfos(), defaultTableInfo.getColumnInfos());
		defaultTableInfo.setColumnInfos(columnInfos);
		return defaultTableInfo;
	}

	private static ColumnInfo[] merge(ColumnInfo[] userColumnInfos, ColumnInfo[] defaultColumnInfos) {
		for (ColumnInfo defaultColumnInfo : defaultColumnInfos) {
			ColumnInfo userColumnInfo = getColumnInfo(defaultColumnInfo.getFieldName(), userColumnInfos);
			defaultColumnInfo = merge(userColumnInfo, defaultColumnInfo);
		}
		return defaultColumnInfos;
	}

	private static ColumnInfo merge(ColumnInfo userColumnInfo, ColumnInfo defaultColumnInfo) {
		if (userColumnInfo != null) {
			Alignment userAlignment = userColumnInfo.getAlignment();
			if (userAlignment != null) {
				defaultColumnInfo.setAlignment(userAlignment);
			}
			Integer userWidth = userColumnInfo.getWidth();
			if (userWidth != null) {
				defaultColumnInfo.setWidth(userWidth);
			}
		}
		return defaultColumnInfo;
	}

	private static ColumnInfo getColumnInfo(String fieldName, ColumnInfo[] columnInfos) {
		ColumnInfo result = null;
		for (ColumnInfo columnInfo : columnInfos) {
			if (columnInfo.getFieldName().equals(fieldName)) {
				result = columnInfo;
				break;
			}
		}
		return result;
	}

	private static XStream configureXStream() {
		XStream xstream = new XStream();
		xstream.alias("preferences", Preferences.class);
		xstream.alias("table", TableInfo.class);
		xstream.alias("column", ColumnInfo.class);
		xstream.registerConverter(TableInfoConverter.getInstance());
		xstream.registerConverter(ColumnInfoConverter.getInstance());
		return xstream;
	}
}