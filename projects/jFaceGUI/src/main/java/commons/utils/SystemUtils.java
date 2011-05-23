package commons.utils;

import java.io.File;
import java.io.IOException;

/*
 * TODO: Make this work in other OS, not only in Windows.
 */
public abstract class SystemUtils {

	private static final String WIN_CMD = System.getProperty("os.name").toUpperCase().startsWith("WINDOWS 9") ? "COMMAND.COM"
			: "cmd.exe";

	public static void exec(String command, final String... args) throws IOException, InterruptedException {
		String[] cmdArgs = new String[5 + args.length];
		int cmdArgIndex = 0;
		// cmd.exe => Starts a new instance of Windows Command interpreter.
		cmdArgs[cmdArgIndex++] = WIN_CMD;
		// /C => Executes the specified command (chained) and then, it finishes.
		cmdArgs[cmdArgIndex++] = "/c";
		// Start => Opens a new window to execute a program or command.
		cmdArgs[cmdArgIndex++] = "start";
		// "title" => Text to be shown in the title bar. Do not change this!
		cmdArgs[cmdArgIndex++] = "\"\"";
		cmdArgs[cmdArgIndex++] = command;
		for (int i = 0; i < args.length; i++) {
			cmdArgs[cmdArgIndex++] = args[i];
		}
		Runtime.getRuntime().exec(cmdArgs).waitFor();
	}

	public static void showFile(String fileName) throws IOException, InterruptedException {
		fileName = StringUtilities.concat("\"", new File(fileName).getCanonicalPath(), "\"");
		String[] args = { WIN_CMD, "/c", "start", "\"\"", fileName };
		Runtime.getRuntime().exec(args).waitFor();
	}

	public static void showFileWithExcel(String fileName) throws IOException, InterruptedException {
		fileName = StringUtilities.concat("\"", new File(fileName).getCanonicalPath(), "\"");
		String[] args = { WIN_CMD, "/c", "start", "excel", "/MAX", "/e", "\"\"", fileName };
		Runtime.getRuntime().exec(args).waitFor();
	}
}
