package commons.utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import commons.properties.CommonConstants;

public abstract class FileHelper {

	public static Properties getProperties(Class clazz, String fileName) {
		Properties props = new Properties();
		try {
			props.load(clazz.getResourceAsStream(fileName));
		} catch (IOException exc) {
			log.fatal(null, exc);
		}
		return props;
	}

	public static String getFileSeparator() {
		return File.separator;
	}

	public static final String OUTPUT_DIR;

	static {
		// Lista de directorios en orden de preferencia
		final String[] dirs = {
		// User's application data directory (only under MS-Windows)
				System.getenv("APPDATA"),
				// User's home directory
				System.getProperty("user.home"),
				// User's current working directory
				System.getProperty("user.dir"),
				// Default temp file path
				System.getProperty("java.io.tmpdir"), };

		String outputDir = null;
		File file;
		for (String dir : dirs) {
			if (dir != null) {
				file = new File(dir);
				if (file.exists() && file.isDirectory()) {
					file = new File(file, CommonConstants.APP_CODE_NAME.toString());
					file.mkdir();
					if (file.exists() && file.isDirectory() && file.canWrite()) {
						outputDir = file.getAbsolutePath();
						break;
					}
				}

			}
		}
		OUTPUT_DIR = outputDir;
	}

	private static final Log log = LogFactory.getLog(FileHelper.class);
}