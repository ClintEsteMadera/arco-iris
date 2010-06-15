/**
 * Created July 2, 2006.
 */
package org.sa.rainbow.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sa.rainbow.core.IDisposable;
import org.sa.rainbow.core.IRainbowRunnable;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.health.IRainbowHealthProtocol;
import org.sa.rainbow.model.Model;
import org.sa.rainbow.translator.IBashBasedScript;

/**
 * Utility helper class, cannot be instantiated.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class Util {

//////////////////////////////////////////////
// Path and I/O data and utility methods
//
	public static final String FILESEP = "/";
	public static final String PATHSEP = ":";
	public static final String LOGEXT = ".log";
	public static final String NEWLINE = Rainbow.NEWLINE;
	public static final String NEWLINE_WIN = Rainbow.NEWLINE_WIN;
	public static final String AT = "@";
	public static final String DOT = ".";
	public static final String DASH = "-";
	public static final String SIZE_SFX = ".size";
	public static final int MAX_BYTES = Rainbow.MAX_BYTES;
	/** Sets the limit on StringBuffer to a threshold max, above which a new
	 *  StringBuffer should be constructed using less than 1/8 of original. */
	public static final int MAX_STRING_BUFFER_LENGTH = 8*64000;  // max of 500KB

	/**
	 * Generates and returns a unique identifier using the supplied class, and
	 * the value of the rainbow property named by
	 * {@linkplain Rainbow.PROPKEY_DEPLOYMENT_LOCATION}.
	 * @param clazz  the class object
	 * @return String  the concatenated string forming the unique identifier
	 */
	public static String genID (Class<?> clazz) {
		return genID(clazz.getSimpleName());
	}
	/**
	 * Generates and returns a unique identifier using the supplied name, and
	 * the value of the rainbow property named by
	 * {@linkplain Rainbow.PROPKEY_DEPLOYMENT_LOCATION}.
	 * @param name  the name to use
	 * @return String  the concatenated string forming the unique identifier
	 */
	public static String genID (String name) {
		return genID(name, Rainbow.property(Rainbow.PROPKEY_DEPLOYMENT_LOCATION));
	}
	/**
	 * Generates and returns a unique identifier composed of name@target,
	 * target converted to lowercase.
	 * @param name    the element name
	 * @param target  the element's location in lowercase
	 * @return String  the concatenated string forming the unique identifier
	 */
	public static String genID (String name, String target) {
		return name + AT + target.toLowerCase();
	}
	public static Pair<String,String> decomposeID (String id) {
		String name = null;
		String loc = null;
		int atIdx = id.indexOf(AT);
		if (atIdx > -1) {  // got both name and target location
			name = id.substring(0, atIdx);
			loc = id.substring(atIdx + AT.length()).toLowerCase();
		} else {  // name only
			name = id;
		}
		return new Pair<String,String>(name, loc);
	}

	public static File getRelativeToPath (File parent, String relPath) {
		File newF = null;
		try {
			newF = new File(parent, relPath).getCanonicalFile();
		} catch (IOException e) {
			System.err.println("Failed getting relative to path == " + relPath);
			e.printStackTrace();
		}
		return newF;
	}
	public static File computeBasePath (final String configPath) {
		// determine path to target config dir
		File basePath = new File(System.getProperty("user.dir"));
		System.out.println("CWD: " + basePath + ", looking for config \"" + configPath + "\"");
		FilenameFilter configFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.equals(configPath);
			}
		};
		// try for the CONFIG path in this path (if invoked from Jar), or parent path
		File[] list = basePath.listFiles(configFilter);
		if (list.length == 0) {  // try parent
			list = basePath.getParentFile().listFiles(configFilter);
		}
		if (list.length > 0) {
			basePath = list[0];
		} else {
			basePath = null;
		}
		return basePath;
	}
	public static String unifyPath (String path) {
		if (File.separator != FILESEP) {  // replace Microsoft backslash sep
			return path.replace(File.separator, FILESEP);
		} else {
			return path;
		}
	}

	/**
	 * Returns the String indicating the IP address of the host on which this
	 * Probe runs.  
	 * @return String  the IP address of the local host
	 */
	public static String localhostIP () {
		String hostip = null;
		try {
			InetAddress ia = InetAddress.getLocalHost();
			hostip = ia.getHostAddress();
		} catch (UnknownHostException e) {
			hostip = null;
		}
		if (hostip == null) {  // use "localhost"
			hostip = "localhost";
		}
		return hostip;
	}

	public static final String TOKEN_BEGIN = "${";
	public static final String TOKEN_END = "}";
	private static final String LB_ESC = "\\$\\{";  // make sure this corresponds
	private static final String RB_ESC = "\\}";  // make sure this corresponds
	private static Pattern m_substitutePattern = Pattern.compile(".*" + LB_ESC + "(.+?)" + RB_ESC + ".*");
	/**
	 * Performs substitution of all values that contains the pattern ${x} with
	 * the value of "x", evaluating against the Rainbow properties
	 * @param str    the String within which to evaluate substitution
	 * @param props  the set of properties to evaluate against, or Rainbow properties if none provided
	 * @return String  the resulting, or original, String after substitution
	 */
	public static String evalTokens (String str, Properties props) {
		if (str == null) return str;

		String result = str;
		Matcher m = m_substitutePattern.matcher(str);
		while (m.find()) {  // substitute!
			String subsKey = m.group(1);
			String tgt = TOKEN_BEGIN + subsKey + TOKEN_END;
			String tgtVal = (props == null) ? Rainbow.property(subsKey) : props.getProperty(subsKey);
			if (tgtVal == null) {
				m_logger.error("BAD: Undefined Rainbow property for token substitution: \"" + subsKey + "\" in \"" + str + "\"");
			} else {
				result = str.replace(tgt, tgtVal);
			}
		}
		return result;
	}
	public static String evalTokens (String str) {
		if (str == null) return str;
		return evalTokens(str, null);
	}
	public static final String MODEL_TOKEN_BEGIN = "@{";
	public static final String MODEL_TOKEN_END = "}";
	private static final String MODEL_LB_ESC = "@\\{";  // make sure this corresponds
	private static final String MODEL_RB_ESC = "\\}";  // make sure this corresponds
	private static Pattern m_modelSubstitutePattern = Pattern.compile(".*" + MODEL_LB_ESC + "(.+?)" + MODEL_RB_ESC + ".*");
	/**
	 * Performs substitution of all values that contains the pattern @{x} with
	 * the property value of the model property "x". 
	 * @param str  the String within which to evaluate substitution
	 * @param model  the model against which to evaluate substitution
	 * @return String  the resulting String with all the model properties substituted
	 */
	public static String evalModelProperties (String str, Model model) {
		if (str == null) return str;

		String result = str;
		Matcher m = m_modelSubstitutePattern.matcher(str);
		while (m.find()) {  // substitute!
			String subsKey = m.group(1);
			String tgt = MODEL_TOKEN_BEGIN + subsKey + MODEL_TOKEN_END;
			String tgtVal = model.getStringProperty(subsKey);
			if (tgtVal != null) {
				result = str.replace(tgt, tgtVal);
			}
		}
		return result;
	}

	private static final int MAX_LINES_PER_RUN = 8;
	private static byte[] bytes = new byte[MAX_BYTES];
	/**
	 * Reads all available lines from the given input (where a line is treated
	 * as a sequenece of characters delimited by a NEWLINE, \n or \r\n), using
	 * buf as the working StringBuffer.  Each line is queued in the queue.
	 * @param input  the InputStream to read from
	 * @param buf    the working StringBuffer to build a line, should not be shared with other inputs
	 * @param queue  the queue to store complete lines
	 * @throws IOException
	 */
	public static void queueLinesFromStream (InputStream input, StringBuffer buf,
			Queue<String> queue) throws IOException {
		int i = MAX_LINES_PER_RUN;
		while ((input.available() > 0 || buf.length() > 0)&& i-- > 0) {
			String line = readLineFromStream(input, buf);
			if (line == null) break;
			queue.offer(line);
		}
	}
	/**
	 * Reads all available lines from the given input (where a line is treated
	 * as a sequenece of characters delimited by a NEWLINE, \n or \r\n), using
	 * buf as the working StringBuffer.  Each line is queued along with its
	 * source input stream in the queue.
	 * @param input  the InputStream to read from
	 * @param buf    the working StringBuffer to build a line, should not be shared with other inputs
	 * @param queue  the queue to store pairs of complete line and its input source
	 * @throws IOException
	 */
	public static void queueLineSourcePairsFromStream (InputStream input, StringBuffer buf,
			Queue<Pair<String,InputStream>> queue) throws IOException {
		int i = MAX_LINES_PER_RUN;
		while ((input.available() > 0 || buf.length() > 0)&& i-- > 0) {
			String line = readLineFromStream(input, buf);
			if (line == null) break;
			queue.offer(new Pair<String,InputStream>(line, input));
		}
	}
	/**
	 * Reads zero or one available line from the given input (where a line is
	 * treated as a sequenece of characters delimited by a NEWLINE, \n or \r\n),
	 * using buf as the working StringBuffer.  Even if no new line is read, the
	 * buffer gets processed for the next line.  A complete line is returned,
	 * or NULL if no line is read.
	 * @param input  the InputStream to read from
	 * @param buf    the working StringBuffer to build a line, should not be shared with other inputs
	 * @return String  the complete line, if newline reached
	 * @throws IOException
	 */
	public static String readLineFromStream (InputStream input, StringBuffer buf)
			throws IOException {
		String line = null;
		if (input.available() > 0 && buf.length() < MAX_STRING_BUFFER_LENGTH) {
			// read available bytes, or MAX_BYTES, which ever is less;
			int cnt = input.read(bytes, 0, Math.min(input.available(), MAX_BYTES));
			buf.append(new String(bytes, 0, cnt));
		}
		int idxUnxNL = 0, idxWinNL = 0, idx = 0, lenNL = 0;
		// search for Un*x and Windows newline since both are possible
		idxUnxNL = buf.indexOf(NEWLINE);
		idxWinNL = buf.indexOf(NEWLINE_WIN);
		if (idxUnxNL > -1) {
			if (idxWinNL > -1 && idxWinNL == idxUnxNL-1) {  // Windows newline!
				idx = idxWinNL;
				lenNL = NEWLINE_WIN.length();
			} else {
				idx = idxUnxNL;
				lenNL = NEWLINE.length();
			}
			line = buf.substring(0, idx);
			buf.delete(0, idx+lenNL);
		}
		return line;
	}
	/**
	 * Checks the buffer length to ensure that it is within limit.  If length
	 * should exceed the limit, then create a new buffer and transfer only 1/8
	 * the content.
	 * @return StringBuffer  new buffer if old buffer has exceeded length, old buffer otherwise.
	 */
	public static StringBuffer checkBufferLength (StringBuffer buf) {
		StringBuffer newBuf = buf;
		if (buf.length() > MAX_STRING_BUFFER_LENGTH) {
			// exceeding length, let's create a new buffer at 1/8 the size
			int startIdx = MAX_STRING_BUFFER_LENGTH * 7 / 8 - 1;
			// search for nearest newline, both Un*x and Windows
			int idxUnxNL = buf.indexOf(NEWLINE, startIdx);
			int idxWinNL = buf.indexOf(NEWLINE_WIN, startIdx);
			if (idxUnxNL > -1) {
				if (idxWinNL > -1 && idxWinNL == idxUnxNL-1) {  // Windows newline!
					startIdx = idxWinNL + NEWLINE_WIN.length();
				} else {
					startIdx = idxUnxNL + NEWLINE.length();
				}
			}  // if not found, then we just chop at the 7/8-length point
			newBuf = new StringBuffer(buf.substring(startIdx));
		}
		return newBuf;
	}
	/**
	 * Reads zero or one available line from the given input (where a line is
	 * treated as a sequenece of characters delimited by a NEWLINE, \n or \r\n),
	 * using a ByteBuffer.
	 * Even if no new line is read, the buffer gets processed for the next line.
	 * A complete line is returned, or NULL if no line is read.
	 * @param input  the InputStream to read from
	 * @param buf    the working ByteBuffer to build a line, should not be shared with other inputs
	 * @return String  the complete line, if newline reached
	 * @throws IOException
	 */
	public static String readLineFromStream (InputStream input, ByteBuffer buf)
			throws IOException {
		String line = null;
		if (input.available() > 0 && buf.hasRemaining()) {
			// read available bytes, or buffer's remaining room, which ever is less;
			int cnt = input.read(bytes, 0, Math.min(input.available(), buf.remaining()));
			buf.put(bytes, 0, cnt);
		}
		// search for Un*x and Windows newline since both are possible
		for (int i = 0; i < buf.position(); ++i) {
			if (buf.get(i) == NEWLINE.charAt(0)){  // found Un*x-style newline
				int lineLen = 0;
				if (buf.get(i-1) == NEWLINE_WIN.charAt(0)) {
					// actually, Windows-style newline
					for (int j = 0; j < i-1; ++j) {
						bytes[lineLen++] = buf.get(j);
					}
				} else {
					for (int j = 0; j < i; ++j) {
						bytes[lineLen++] = buf.get(j);
					}
				}
				// extract line and discard those bytes from buffer
				line = new String(bytes, 0, lineLen);
				buf.position(i + NEWLINE.length());
				buf.compact();  // shifts bytes down from position to 0
				break;
			}
		}
		return line;
	}

	/**
	 * Reads and returns all available output from a given Process.
	 * @param p  the process to read output from
	 * @return String  the textual output, with any MS-Dos newline replaced
	 */
	public static String getProcessOutput (Process p) {
		String str = "";
		try {
			StringBuffer buf = new StringBuffer();
			byte[] bytes = new byte[Util.MAX_BYTES];
			BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
			while (bis.available() > 0) {
				int cnt = bis.read(bytes);
				buf.append(new String(bytes, 0, cnt));
			}
			str = buf.toString().replace(NEWLINE_WIN, NEWLINE);
		} catch (IOException e) {
			logger().error("Get process output failed!", e);
		}
		return str;
	}

	public static void setExecutablePermission (String path) {
		String[] cmds = { IBashBasedScript.LINUX_CHMOD, IBashBasedScript.CHMOD_OPT, path };
		ProcessBuilder pb = new ProcessBuilder(cmds);
		pb.redirectErrorStream(true);
		try {
			Process p = pb.start();
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				m_logger.error("chmod interrupted?", e);
			}
			String pOut = Util.getProcessOutput(p);
			if (pOut.length() > 2) {  // probably some useful output
				logger().info("- Chmod output: " + pOut);
			}
		} catch (IOException e) {
			m_logger.error("Process I/O failed!", e);
		}
	}

	public static String computeMD5Sum (InputStream in) {
		String md5sum = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = new byte[MAX_BYTES];
			while (in.available() > 0) {
				int cnt = in.read(bytes, 0, Math.min(in.available(), MAX_BYTES));
				md.update(bytes, 0, cnt);
			}
			byte[] md5 = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i=0; i < md5.length; ++i) {
				String tmp = "0" + Integer.toHexString( (0xff & md5[i]));
				sb.append(tmp.substring(tmp.length()-2));
			}
			md5sum = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			dataLogger().error("No algorithm to compute md5sum on stream.", e);
		} catch (IOException e) {
			dataLogger().error("Compute md5sum on stream failed!", e);
		}
		return md5sum;
	}

	public static String computeMD5sum (byte[] bytes) {
		String md5sum = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(bytes);
			StringBuffer sb = new StringBuffer();
			for (int i=0; i < md5.length; ++i) {
				String tmp = "0" + Integer.toHexString( (0xff & md5[i]));
				sb.append(tmp.substring(tmp.length()-2));
			}
			md5sum = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			dataLogger().error("No algorithm to compute md5sum on bytes.", e);
		}
		return md5sum;
	}

	/**
	 * Lists Classes inside a given package.
	 * @author Jon Peck http://jonpeck.com (adapted from http://www.javaworld.com/javaworld/javatips/jw-javatip113.html)
	 * @param pkgname String name of a Package, e.g., "java.lang"
	 * @return Class[]  classes inside the root of the given package 
	 * @throws ClassNotFoundException if the package name points to an invalid package
	 */
	public static Class[] getClasses (String pkgname) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		// either get a File or JarFile object for the package
		String pkgdir = pkgname.replace('.', '/');
		File directory = null;
		JarFile jarFile = null;
		try {
			URL pkgURL = Thread.currentThread().getContextClassLoader().getResource(pkgdir);
			logger().debug("*~* Got resource: " + pkgURL.toExternalForm());
			// see if file is inside a JAR
			if (pkgURL.getProtocol().equalsIgnoreCase("jar")) {
				// obtain the Jar URL connection to get the JAR file
				JarURLConnection jarConn = (JarURLConnection )pkgURL.openConnection();
				jarFile = jarConn.getJarFile();
			} else {
				directory = new File(pkgURL.getFile());
			}
		} catch (Exception e) {  // expect NullPointerException or IOException
			String msg = pkgname + " does not appear to be a valid package!";
			logger().error(msg, e);
			throw new ClassNotFoundException(pkgname + " does not appear to be a valid package!", e);
		}
		if (directory != null && directory.exists()) {  // search ordinary directory
			// get the list of files contained in the package
			String[] files = directory.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					// we're only interested in .class files
					return name.endsWith(".class");
				}
			});
			for (String fname : files) {  // remove .class extension and add class
				classes.add(Class.forName(pkgname + '.' + fname.substring(0, fname.length()-6)));
			}
		} else if (jarFile != null) {  // sift through a jar file
			// look for files that has 'pkgname' after ! and ends with class
			Pattern p = Pattern.compile("[!]/" + pkgname + "/(.+)[.]class");
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry e = entries.nextElement();
				Matcher m = p.matcher(e.getName());
				if (m.matches()) {  // found one
					classes.add(Class.forName(pkgname + '.' + m.group(1)));
				}
			}
		} else {
			throw new ClassNotFoundException(pkgname + " package does not correspond to an existent directory!");
		}

		Class<?>[] classArray = new Class<?>[classes.size()];
		classes.toArray(classArray);
		return classArray;
	}


//////////////////////////////////////////////
// Date and Time utility methods
//
	private static final SimpleDateFormat m_timestampFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	/** Returns current timestamp string of the form yyyyMMddHHmmssSSS */
	public static String timestamp () {
		return m_timestampFormat.format(Calendar.getInstance().getTime());
	}
	private static final SimpleDateFormat m_timestampShortFormat = new SimpleDateFormat("MMddHHmmssSSS");
	/** Returns current timestamp string of the form MMddHHmmssSSS */
	public static String timestampShort () {
		return m_timestampShortFormat.format(Calendar.getInstance().getTime());
	}
	private static final SimpleDateFormat m_timelogFormat = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss.SSSZ");
	/** Returns current timestamp string of the form yyyy.MM.dd-HH:mm:ss.SSSZ */
	public static String timelog () {
		return m_timelogFormat.format(Calendar.getInstance().getTime());
	}
	private static final SimpleDateFormat m_probeLogFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
	/** Returns current timestamp string of the form EEE MMM d HH:mm:ss yyyy */
	public static String probeLogTimestamp () {
		return m_probeLogFormat.format(Calendar.getInstance().getTime());
	}
	/** Returns timestamp string of the form EEE MMM d HH:mm:ss yyyy for the supplied time in milliseconds */
	public static String probeLogTimestampFor (long milliseconds) {
		return m_probeLogFormat.format(milliseconds);
	}

	public static void pause (long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// intentionally ignore
		}
	}
	public static void waitUntilDisposed (IDisposable disposable) {
		while (! disposable.isDisposed()) {
			try {
				Thread.sleep(IRainbowRunnable.SLEEP_TIME);
			} catch (InterruptedException e) {
				// intentional ignore
			}
		}
	}


//////////////////////////////////////////////
// Math data and utility methods
//
	private static final int SIZE = 100;
	private static long[] m_facts = null;
	/**
	 * Compute n-factorial using memoization.
	 * @param n  value for which to compute factorial, must be smaller than SIZE.
	 * @return n-factorial
	 */
	public static long factorial (int n) {
		if (n > SIZE-1) return 0L;  // can't compute
		if (m_facts == null) {  // initialize memoization array
			m_facts = new long[SIZE];
			// termination-case values
			m_facts[0] = 1;
			m_facts[1] = 1;
		}

		if (n > 1) {
			if (m_facts[n] == 0) {
				m_facts[n] = n * factorial(n-1);
			}
		}
		return m_facts[n];
	}
	/** Returns the memoized factorial value, mainly for testing purpose. */
	public static long memoizedFactorial (int n) {
		return m_facts[n];
	}
	/** Prints object as string to the output stream. */
	public static void print (Object o) {
		System.out.println(o.toString());
	}

//////////////////////////////////////////////
// Type manipulation utility
//
	private static Map<String,Class<?>> m_primName2Class = null;
	private static Map<Class<?>,Class<?>> m_primClass2Class = null;
	private static void lazyInitMaps () {
		if (m_primName2Class != null) return;  // already init'd
		// Map of primitive type names to Java Class
		m_primName2Class = new HashMap<String,Class<?>>();
		m_primName2Class.put("int", Integer.class);
		m_primName2Class.put("short", Short.class);
		m_primName2Class.put("long", Long.class);
		m_primName2Class.put("float", Float.class);
		m_primName2Class.put("double", Double.class);
		m_primName2Class.put("boolean", Boolean.class);
		m_primName2Class.put("byte", Byte.class);
		m_primName2Class.put("char", Character.class);
		// Map of primitive type class to Java Class
		m_primClass2Class = new HashMap<Class<?>,Class<?>>();
		m_primClass2Class.put(int.class, Integer.class);
		m_primClass2Class.put(short.class, Short.class);
		m_primClass2Class.put(long.class, Long.class);
		m_primClass2Class.put(float.class, Float.class);
		m_primClass2Class.put(double.class, Double.class);
		m_primClass2Class.put(boolean.class, Boolean.class);
		m_primClass2Class.put(byte.class, Byte.class);
		m_primClass2Class.put(char.class, Character.class);
	}
    public static Object parseObject (String val, String classStr) {
		Object valObj = val;  // by default, return just the String value
		try {
			Class<?> typeClass = Class.forName(classStr);
			// try first parsing a primitive value
			valObj = parsePrimitiveValue(val, typeClass);
			if (valObj == val) {  // now trying instantiating the class
				valObj = typeClass.newInstance();
			}
		} catch (ClassNotFoundException e) {
			// see if it's one of the primitive types
			lazyInitMaps();
			valObj = parsePrimitiveValue(val, m_primName2Class.get(classStr.intern()));
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		return valObj;
	}
	public static Object parsePrimitiveValue (String val, Class<?> clazz) {
		if (clazz == null) return val;

		Object primVal = val;  // by default, return the String value
		if (clazz.isPrimitive()) {  // get the Java Class
			lazyInitMaps();
			clazz = m_primClass2Class.get(clazz);
		}
		// look for a valueOf(String) method, since the eight Classes that
		// represent the primitives each has such a static method
		Class<?>[] paramTypes = new Class<?>[1];
		paramTypes[0] = String.class;
		try {  // catch and ignore any exception, which we treat as failing to "convert"
			Method m = clazz.getDeclaredMethod("valueOf", paramTypes);
			Object[] args = new Object[1];
			args[0] = val;
			primVal = m.invoke(null, args);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		return primVal;
	}


//////////////////////////////////////////////
// Debug settings and utility methods
//
	public static final String DEFAULT_LEVEL = "WARN";
	/** ISO8601 prints the format "yyyy-MM-dd HH:mm:ss,SSS" */
    public static final String DEFAULT_PATTERN = "%d{ISO8601} [%t] %p %c %x - %m%n";
    public static final int DEFAULT_MAX_SIZE = 1024;
    public static final int DEFAULT_MAX_BACKUPS = 5;
    public static final String CONSOLE_PATTERN = "[%d{ISO8601}] %m%n";
    public static final String DATA_LOGGER_NAME = "DataLogger";
    public static final String DATA_PATTERN = "%d{ISO8601} %m%n";

	private static RainbowLogger m_logger = null;
	private static RainbowLogger m_dataLogger = null;

    public static Properties defineLoggerProperties () {
        String filepath = Rainbow.property(Rainbow.PROPKEY_LOG_PATH);
		if (! filepath.startsWith("/")) {
			filepath = getRelativeToPath(Rainbow.instance().getTargetPath(), filepath).toString();
		}
		String datapath = Rainbow.property(Rainbow.PROPKEY_DATA_LOG_PATH);
		if (! datapath.startsWith("/")) {
			datapath = getRelativeToPath(Rainbow.instance().getTargetPath(), datapath).toString();
		}

        // setup logging
        Properties props = new Properties ();
        props.setProperty("log4j.appender.FileLog", "org.apache.log4j.RollingFileAppender");
        props.setProperty("log4j.appender.FileLog.layout", "org.apache.log4j.PatternLayout");
        props.setProperty("log4j.appender.FileLog.layout.ConversionPattern", Rainbow.property(Rainbow.PROPKEY_LOG_PATTERN, DEFAULT_PATTERN));
        props.setProperty("log4j.appender.FileLog.MaxFileSize", Rainbow.property(Rainbow.PROPKEY_LOG_MAX_SIZE, String.valueOf(DEFAULT_MAX_SIZE)) + "KB");
        props.setProperty("log4j.appender.FileLog.MaxBackupIndex", Rainbow.property(Rainbow.PROPKEY_LOG_MAX_BACKUPS, String.valueOf(DEFAULT_MAX_BACKUPS)));
        props.setProperty("log4j.appender.FileLog.File", filepath);
        props.setProperty("log4j.appender.ConsoleLog","org.apache.log4j.ConsoleAppender");
        props.setProperty("log4j.appender.ConsoleLog.Target","System.out");
        props.setProperty("log4j.appender.ConsoleLog.layout","org.apache.log4j.PatternLayout");
        props.setProperty("log4j.appender.ConsoleLog.layout.ConversionPattern", CONSOLE_PATTERN);
        String rootSetting = Rainbow.property(Rainbow.PROPKEY_LOG_LEVEL, DEFAULT_LEVEL) + ",FileLog,ConsoleLog";
        props.setProperty("log4j.rootLogger", rootSetting);
        // setup data logging, using trace level
        props.setProperty("log4j.appender.DataLog", "org.apache.log4j.RollingFileAppender");
        props.setProperty("log4j.appender.DataLog.layout", "org.apache.log4j.PatternLayout");
        props.setProperty("log4j.appender.DataLog.layout.ConversionPattern", DATA_PATTERN);
        props.setProperty("log4j.appender.DataLog.MaxFileSize", Rainbow.property(Rainbow.PROPKEY_LOG_MAX_SIZE, String.valueOf(DEFAULT_MAX_SIZE)) + "KB");
        props.setProperty("log4j.appender.DataLog.MaxBackupIndex", Rainbow.property(Rainbow.PROPKEY_LOG_MAX_BACKUPS, String.valueOf(DEFAULT_MAX_BACKUPS)));
        props.setProperty("log4j.appender.DataLog.File", datapath);
        props.setProperty("log4j.logger." + DATA_LOGGER_NAME, "INFO,DataLog");
        // don't invoke ancester appenders
        props.setProperty("log4j.additivity." + DATA_LOGGER_NAME, "false");

        return props;
	}
	/**
	 * Returns the currently available Rainbow Logger.
	 * @return
	 */
	public static RainbowLogger logger () {
		if (m_logger == null) {
			m_logger = RainbowLoggerFactory.logger(Util.class);
		}
		return m_logger;
	}
	public static RainbowLogger dataLogger () {
		if (m_dataLogger == null) {
			m_dataLogger = RainbowLoggerFactory.logger(DATA_LOGGER_NAME);
		}
		return m_dataLogger;
	}
    /**
     * Throws and catches a {@link Throwable}, and then reports the stack. This is
     * useful for finding call traces
     * 
     * @param message  the message to report
     */
    public static void reportStack (String message) {
        logger().trace(message);
        try {
            throw new Throwable (message);
        } catch (Throwable t) {
            logger().trace("", t);
        }
    }

    public static void reportMemUsage () {
    	StringBuffer usageStr = new StringBuffer(IRainbowHealthProtocol.DATA_MEMORY_USE);
    	usageStr.append(Runtime.getRuntime().freeMemory()).append(" ");
    	usageStr.append(Runtime.getRuntime().totalMemory()).append(" ");
    	usageStr.append(Runtime.getRuntime().maxMemory()).append(" ");
    	dataLogger().info(usageStr.toString());
    }

}
