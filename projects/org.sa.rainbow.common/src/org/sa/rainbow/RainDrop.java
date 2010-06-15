package org.sa.rainbow;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * A daemon process for managing the starting and restarting of the localhost
 * Rainbow processes.  This daemon is intentionally designed to be minimalist.
 * It depends on no other class, except values derive from Rainbow properties
 * and constants, now defined in the common RainbowConstants intefrace.
 * This isolation is done to allow the RainDrop daemon to be deployed standalone.
 * <p> 
 * The daemon continually spawns the latest Rainbow jar file in the "drop"
 * until it is instructed to "self-destruct" through Rainbow's return value,
 * a normal Rainbow exit.
 * <p>
 * For security reasons, this class should be examined and improved because it
 * is a potential hole for arbitrary Java execution.  The main protection
 * is in the Rainbow service port communication mechanism, which depends on
 * attributes that could be circumvented, e.g., checking socket IP to make
 * sure it's from the Master, etc.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class RainDrop {

	public static final String NAME = "RainDropD";

	/** Loop sleep time, in milliseconds. */
	public static final int SLEEP_TIME = 100;

	/** The name of the output dump file. */
	public static final String DUMP_FILE = "out.log";
	/** The name of the configuration init property file. */
	public static final String CONFIG_FILE = "config.ini";
	/** The name of property for max Java heap size. */
	public static final String PROPKEY_MAX_HEAP = "max.heap.size";
	/** The default max Java heap size. */
	public static final String DEFAULT_MAX_HEAP = "64M";
	/** The name of property for optional Java VM options. */
	public static final String PROPKEY_VM_OPTIONS = "vm.options";
	/** The name of the Rainbow Master Main Class. */
	public static final String RAINBOW_MASTER_MAIN = "org.sa.rainbow.core.Oracle";
	/** The name of the Rainbow Delegate Main Class. */
	public static final String RAINBOW_DELEGATE_MAIN = "org.sa.rainbow.translator.RainbowDelegate";
	/** The filename stub of the JAR to invoke. */
	public static final String RAINBOW_JAR_NAME_STUB = "app.rainbow.";
	/** The partial stub name for the master JAR. */
	public static final String MASTER_NAME_STUB = "master";
	/** The partial stub name for the delegate JAR. */
	public static final String DELEGATE_NAME_STUB = "delegate";
	/** The filename suffix of JAR. */
	public static final String RAINBOW_JAR_SUFFIX = ".jar";
	/** The filename suffix of ZIP. */
	public static final String RAINBOW_ZIP_SUFFIX = ".zip";

	/** Size of read buffer to allocate. */
	public static final short MAX_BYTES = RainbowConstants.MAX_BYTES;
	/** Delimiting character for the release date. */
	public static final String DATE_DELIM = RainbowConstants.USCORE;
	public static final String NEWLINE = RainbowConstants.NEWLINE;
	public static final String NEWLINE_WIN = RainbowConstants.NEWLINE_WIN;

	private static String m_cwd = null;
	private static PrintWriter m_dumpOut = null;
	private static boolean m_execDelegate = true;
	private static int m_errorCnt = 0;

	private static byte[] m_bytes = new byte[MAX_BYTES];

	private static class SingleNameFilter implements FilenameFilter {
		private String m_matchName = null;
		public SingleNameFilter (String matchName) {
			m_matchName = matchName;
		}
		public boolean accept (File dir, String name) {
			return name.equals(m_matchName);
		}
	}

	/**
	 * @param args
	 */
	public static void main (String[] args) {
		// setup a couple env-related variables
		m_cwd = System.getProperty("user.dir");
		try {
			m_dumpOut = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(m_cwd, DUMP_FILE), true)));
		} catch (FileNotFoundException e) {
			printException(e);
		}
		println("====================");
		printRainDrop("CWD: " + m_cwd);

		// pick one of the non-destruct exit values to keep loop running
		int rv = RainbowConstants.EXIT_VALUE_RESTART;
		while (rv != RainbowConstants.EXIT_VALUE_DESTRUCT) {
			rv = spawnRainbow(args);
			printRainDrop("Rainbow exit value: " + rv);
			processUpdates();
			if (rv == RainbowConstants.EXIT_VALUE_RESTART) {
				m_errorCnt = 0;  // assume things ran normally
				printRainDrop("restarting Rainbow...");
			} else if (rv == RainbowConstants.EXIT_VALUE_SLEEP) {
				m_errorCnt = 0;  // assume things ran normally
				printRainDrop("sleeping...ZZZ");
				if (m_dumpOut != null) m_dumpOut.flush();
				rv = sleepUntilSignal();
			} else if (rv == RainbowConstants.EXIT_VALUE_ABORT) {  // uh oh!
				printRainDrop("Rainbow aborted, probably something fatal, sleeping!");
				if (m_dumpOut != null) m_dumpOut.flush();
				rv = sleepUntilSignal();
			}
			if (m_dumpOut != null) m_dumpOut.flush();
		}
		printRainDrop("self-destructing!");
		if (m_dumpOut != null) m_dumpOut.close();
		System.exit(rv);
	}

	private static int spawnRainbow (String[] args) {
		// obtain the initialization property
		Properties props = new Properties();
		try {
			FileInputStream pfin = new FileInputStream(new File(m_cwd, CONFIG_FILE));
			props.load(pfin);
			pfin.close();
		} catch (FileNotFoundException e) {
			printException(e);
			tallyError();
		} catch (IOException e) {
			printException(e);
			tallyError();
		}
		// obtain jar file name
		String dropjar = RainbowConstants.RAINBOW_BIN_DIR + "/" + computeJarName();
		//printRainDrop("spawning Rainbow: " + dropjar);  // info already in classpath output
		// take an initial guess of main class
		String mainClass = (m_execDelegate) ? RAINBOW_DELEGATE_MAIN : RAINBOW_MASTER_MAIN;
/* DO NOT enable this yet, for security reasons
		try {
			JarFile jarFile = new JarFile(dropjar);
			Manifest mf = jarFile.getManifest();
			mainClass = mf.getMainAttributes().getValue("Main-Class");
		} catch (IOException e) {
			// something wrong with the jar file, use our own guess of main class
			printException(e);
		}
*/
		String cfgPath = props.getProperty(RainbowConstants.PROPKEY_CONFIG_PATH, RainbowConstants.RAINBOW_CONFIG_PATH);
		String tgtName = props.getProperty(RainbowConstants.PROPKEY_TARGET_NAME, RainbowConstants.DEFAULT_TARGET_NAME);
		String classpath = computeClassPaths(dropjar, RainbowConstants.RAINBOW_JAR_CONTRIB_DIR);
		printRainDrop("classpath: " + classpath);
		List<String> cmdList = new ArrayList<String>();
		Collections.addAll(cmdList,
				"java",
				"-D" + RainbowConstants.PROPKEY_CONFIG_PATH + "=" + cfgPath,
				"-D" + RainbowConstants.PROPKEY_TARGET_NAME + "=" + tgtName,
				"-Xmx" + props.getProperty(PROPKEY_MAX_HEAP, DEFAULT_MAX_HEAP));
		String vmOpts = props.getProperty(PROPKEY_VM_OPTIONS);
		if (vmOpts != null) {
			Collections.addAll(cmdList, vmOpts.split("\\s+"));
		}
		Collections.addAll(cmdList,
				"-cp", classpath,
				mainClass );
		Collections.addAll(cmdList, args);
		int rv = Integer.MIN_VALUE;
		StringBuffer buf = new StringBuffer();
		try {
			printRainDrop("=== stdout+stderr ===");
			ProcessBuilder pb = new ProcessBuilder(cmdList);
			pb.redirectErrorStream(true);
			
			Process p = pb.start();
			while (rv == Integer.MIN_VALUE) {
				pause(SLEEP_TIME);
				try {
					rv = p.exitValue();
				} catch (IllegalThreadStateException e) {
					// intentional ignore
				}
				// process stdout and stderr
				try {
					buf.delete(0, buf.length());
					BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
					while (bis.available() > 0) {
						int cnt = bis.read(m_bytes);
						buf.append(new String(m_bytes, 0, cnt));
					}
				} catch (IOException e) {
					printException(e);
					tallyError();
				}
				print(buf.toString().replace(NEWLINE_WIN, NEWLINE));
				if (m_dumpOut != null) m_dumpOut.flush();
			}
			p.destroy();  // make sure process is destroyed
			pause(5*SLEEP_TIME);  // pause a little
		} catch (IOException e) {
			printException(e);
			tallyError();
		}
		return rv;
	}

	/**
	 * Scans the bin directory for the rainbow JAR names matching the stub.
	 * If any is found, the most recent of that JAR kind gets invoked.
	 * If more than one variety is present (one of master or delegate),
	 * then the most recent across the varieties is chosen.
	 * In case of a tie, then the order of precedence is "delegate" first,
	 * then "master".
	 * Determining the jar to execute also causes a side-effect on the data
	 * member m_execDelegate, indicating whether the jar is a delegate.
	 * 
	 * @return String  the composed file name of the JAR to invoke
	 */
	private static String computeJarName () {
		// create the filters for files of different JAR name stubs
		FilenameFilter masterFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(RAINBOW_JAR_NAME_STUB + MASTER_NAME_STUB);
			}
		};
		FilenameFilter delegateFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(RAINBOW_JAR_NAME_STUB + DELEGATE_NAME_STUB);
			}
		};
		// list the files using the filters
		File bindir = new File(RainbowConstants.RAINBOW_BIN_DIR);
		File[] listMs = bindir.listFiles(masterFilter);
		File[] listDl = bindir.listFiles(delegateFilter);

		// sort the lists, and retrieve the latest to determine which wins
		File winner = null;
		String dateStr = "";
		if (listDl.length > 0) {  // "delegate" takes highest precedence
			Arrays.sort(listDl, 0, listDl.length);
			winner = listDl[listDl.length-1];
			dateStr = extractDateStr(winner.getName(), RAINBOW_JAR_NAME_STUB + DELEGATE_NAME_STUB);
			//printRainDrop("winner Dl: " + winner.getName() + ", dateStr: " + dateStr);
			m_execDelegate = true;
		}
		if (listMs.length > 0) {  // next comes "master"
			Arrays.sort(listMs, 0, listMs.length);
			File contender = listMs[listMs.length-1];
			String dateStr2 = extractDateStr(contender.getName(), RAINBOW_JAR_NAME_STUB + MASTER_NAME_STUB);
			//printRainDrop("contender Ms: " + contender.getName() + ", dateStr: " + dateStr2);
			if (dateStr2.compareTo(dateStr) > 0) {  // only when date _exceeds_
				// found a new winner
				dateStr = dateStr2;
				winner = contender;
				m_execDelegate = false;
			}
		}
		
		return winner.getName();
	}

	private static String extractDateStr (String name, String stub) {
		String dateStr = null;
		String nameStub = stub + DATE_DELIM;
		int idx = name.indexOf(nameStub);
		if (idx > -1) {
			dateStr = name.substring(nameStub.length());
		} else {
			dateStr = "";
		}
		return dateStr;
	}

	/**
	 * Scans the contribution javalib directory for any jar files, and build them
	 * into a classpath string, using system-specific path separator.
	 * @return String  the classpath string
	 */
	private static String computeClassPaths (String appjar, String jardir) {
		StringBuffer cpBuf = new StringBuffer();
		final String SEP = File.pathSeparator;
		FilenameFilter jarFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(RAINBOW_JAR_SUFFIX);
			}
		};
		FilenameFilter dirFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !(name.equals(".") || name.equals(".."));
			}
		};
		cpBuf.append(appjar).append(SEP).append(jardir);
		File dir = new File(jardir);
		if (dir.exists()) {  // search recursively for JAR files
			// using a Queue, this loop implementation results in jars being
			// scanned in a breadth-first fashion
			Queue<File> dirQ = new LinkedList<File>();
			do {
				// find all JAR files at the current level
				File[] jarList = dir.listFiles(jarFilter);
				for (File f : jarList) {
					cpBuf.append(SEP).append(f.getPath());
				}
				// scan and queue the dirs in this level
				File[] potentialDirList = dir.listFiles(dirFilter);
				for (File pd : potentialDirList) {
					if (pd.isDirectory()) {
						dirQ.offer(pd);
					}
				}
				dir = dirQ.poll();
			} while (dir != null);
		}
		return cpBuf.toString();
	}

	/**
	 * Checks the update directory for any updates, and process those according
	 * to their path and file types.  There are currently three:
	 * <ol>
	 * <li> app.zip
	 * <li> drops/app.jar
	 * <li> lib/<somelib>.jar
	 * </ol>
	 * TODO: Use an XML to provide instruction on what type of updates.
	 */
	private static void processUpdates () {
		FileFilter fileOrNonEmptyDir = new FileFilter() {
			public boolean accept(File path) {
				return path.isFile()
					|| (path.isDirectory() && path.list().length > 0);
			}
		};

		File updatedir = new File(RainbowConstants.RAINBOW_UPDATE_DIR);
		if (! updatedir.exists()) return;  // no update dir to process
		File[] updateList = updatedir.listFiles(fileOrNonEmptyDir);
		if (updateList.length == 0) return;

		printRainDrop("updates, processing " + updateList.length + " entr" + (updateList.length > 1 ? "ies" : "y"));
		// process files/dirs in update dir in turn
		for (File f : updateList) {
			if (f.getName().equals(RainbowConstants.RAINBOW_JAR_CONTRIB_DIR)) {
				// update to go into the jar contrib, may replace existing file
				File lib = new File(RainbowConstants.RAINBOW_JAR_CONTRIB_DIR);
				if (! lib.exists()) {  // unlikely, but create just in case
					printRainDrop("Strange, the lib dir didn't exist?! Creating " + lib.getAbsolutePath());
					lib.mkdir();
				}
				transferContent(f, lib);
			} else if (f.getName().equals(RainbowConstants.RAINBOW_BIN_DIR)) {
				// update to go into the drop, should not replace file due to timestamp
				File drop = new File(RainbowConstants.RAINBOW_BIN_DIR);
				if (! drop.exists()) {  // unlikely, but create just in case
					printRainDrop("Strange, the drops dir didn't exist?! Creating " + drop.getAbsolutePath());
					drop.mkdir();
				}
				transferContent(f, drop);
			} else if (f.getName().endsWith(RAINBOW_ZIP_SUFFIX)) {
				// an entire update zip file, need to worry about locked file
				// unzip and store files separately, taking care to skip locked files
				try {
					ZipFile zip = new ZipFile(f);
					if (zip == null || zip.size() == 0) {  // delete zip file
						f.delete();
						continue;
					}
					printRainDrop("  unzipping " + f.getPath());
					File tgt = new File(".");
					File file = null;
					Enumeration<?> entries = zip.entries();
					while (entries.hasMoreElements()) {
						ZipEntry entry = (ZipEntry )entries.nextElement();
						file = new File(tgt, entry.getName());
						file.getParentFile().mkdirs();
						if (!entry.isDirectory()) {
							boolean goAhead = true;
							try {
								if (file.exists()) {  // try deleting file first
									goAhead = file.delete();
								}
								if (goAhead) {
									InputStream zin = zip.getInputStream(entry);
									OutputStream fout = new FileOutputStream(file);
									while (zin.available() > 0) {
										int cnt = zin.read(m_bytes);
										fout.write(m_bytes, 0, cnt);
									}
									zin.close();
									fout.close();
								} else {
									printErr("  - overwrite failed: " + file.getPath());
								}
							} catch (IOException e) {
								printException(e);
							}
						}
					}
					// close and delete the zip file
					zip.close();
					f.delete();
				} catch (ZipException e) {
					printException(e);
				} catch (IOException e) {
					printException(e);
				}
			}
		}
		printRainDrop("update complete.");
	}

	/**
	 * Uses a Queue to move through the files breadth-first, creating a
	 * corresponding dir on the lib side as needed, and moving files within the
	 * current dir over.
	 *
	 * @param f    source file/dir content
	 * @param tgt  target dir
	 */
	private static void transferContent (File f, File tgt) {
		Queue<File> dirQ = new LinkedList<File>();
		Queue<File> libdirQ = new LinkedList<File>();
		File dir = f;
		File libdir = tgt;
		do {
			for (File fItem : dir.listFiles()) {
				String fName = fItem.getName();
				File[] libList = libdir.listFiles(new SingleNameFilter(fName));
				if (fItem.isDirectory()) {  // add to queue
					File libItem = null;
					// create corresponding dir in lib if necessary
					if (libList.length == 0) {
						libItem = new File(libdir.getPath(), fName);
						libItem.mkdir();
					} else {
						libItem = libList[0];
					}
					// queue lib dir so that we have convenient matching dirs
					libdirQ.offer(libItem);
					dirQ.offer(fItem);
				} else {  // move file to jar side
					File libItem = new File(libdir.getPath(), fName);
					printRainDrop("  copying " + libItem.getPath() + "...");
					boolean goAhead = true;
					try {
						if (libItem.exists()) {  // attempt to delete file first
							goAhead = libItem.delete();
							if (!goAhead) {
								printErr("could not overwrite!");
							}
						}
						// now create the file anew
						if (goAhead && libItem.createNewFile()) {
							// file creation succeeded, write content
							BufferedInputStream fin = new BufferedInputStream(new FileInputStream(fItem));
							BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(libItem));
							while (fin.available() > 0) {
								int cnt = fin.read(m_bytes, 0, MAX_BYTES);
								fout.write(m_bytes, 0, cnt);
								fout.flush();
							}
							fin.close();
							fout.close();
							printRainDrop("good");
							// now delete the source update file
							fItem.delete();
						} else {
							printErr("could not create!");
						}
					} catch (IOException e) {
						printErr("error!");
						printException(e);
						libItem.delete();  // delete erroneous file
					}
				}
			}
			dir = dirQ.poll();
			libdir = libdirQ.poll();
		} while (dir != null && libdir != null);
	}

	/**
	 * Sleeps until a restart signal is received on the Rainbow port.
	 */
	private static int sleepUntilSignal () {
		int rv = RainbowConstants.EXIT_VALUE_RESTART;
		ServerSocket socketServer = null;
		try {
			socketServer = new ServerSocket(RainbowConstants.RAINBOW_SERVICE_PORT);
		} catch (IOException e) {
			printException(e);
			tallyError();
		}
		while (socketServer != null) {
			pause(SLEEP_TIME);
			try {  // wait for a socket connection
				Socket socket = socketServer.accept();
				// read just one byte of int(0) to restart, int(-1) to destruct
				InputStream sockin = socket.getInputStream();
				boolean readOk = false;
				int v = sockin.read();
				switch (v) {
				case 0:  // restart!
					rv = RainbowConstants.EXIT_VALUE_RESTART;
					printRainDrop("awakened!");
					readOk = true;
					break;
				case 1:  // stop completely
					rv = RainbowConstants.EXIT_VALUE_DESTRUCT;
					readOk = true;
					break;
				}
				if (readOk) {
					socketServer.close();
					socketServer = null;
				} 
				// make sure to close socket
				socket.close();
			} catch (IOException e) {
				socketServer = null;
				printException(e);
				tallyError();
			}
		}
		return rv;
	}

	private static void tallyError () {
		++m_errorCnt;
		printRainDrop("Tallied error " + m_errorCnt + " of " + RainbowConstants.MAX_ERROR_CNT + " tolerable errors");

		if (m_errorCnt > RainbowConstants.MAX_ERROR_CNT) {  // self-destruct
			printErr(NAME + " experienced " + m_errorCnt + " internal errors, more than it can tolerate... self-destruct!!");
			System.exit(RainbowConstants.EXIT_VALUE_ABORT);
		}
	}

	private static void printRainDrop (String txt) {
		println("[[" + NAME + "]=> " + txt);
	}
	private static void print (String txt) {
		if (m_dumpOut != null) m_dumpOut.print(txt);
		System.out.print(txt);
	}
	private static void println (String txt) {
		if (m_dumpOut != null) m_dumpOut.println(txt);
		System.out.println(txt);
	}
	private static void printErr (String txt) {
		if (m_dumpOut != null) m_dumpOut.println(txt);
		System.err.println(txt);
	}
	private static void printException (Throwable t) {
		if (m_dumpOut != null) t.printStackTrace(m_dumpOut);
		t.printStackTrace();
	}

	private static void pause (long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// intentionally ignore
		}
	}

}
