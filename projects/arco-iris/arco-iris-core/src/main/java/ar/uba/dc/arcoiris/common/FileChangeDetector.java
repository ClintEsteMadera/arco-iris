package ar.uba.dc.arcoiris.common;

import java.io.File;
import java.util.TimerTask;

public abstract class FileChangeDetector extends TimerTask {
	private long timeStamp;
	private File file;

	public FileChangeDetector(File file) {
		this.file = file;
		this.timeStamp = file.lastModified();
	}

	@Override
	public final void run() {
		long timeStamp = file.lastModified();

		if (this.timeStamp != timeStamp) {
			this.timeStamp = timeStamp;
			onChange(file);
		}
	}

	protected abstract void onChange(File file);
}