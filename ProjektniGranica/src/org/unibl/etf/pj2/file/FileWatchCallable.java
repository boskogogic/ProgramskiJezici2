package org.unibl.etf.pj2.file;

import java.util.concurrent.Callable;

public class FileWatchCallable implements Callable<Void> {

	@Override
	public Void call() throws Exception {
		FileWatcher fw = new FileWatcher();
		fw.watchFolder();

		return null;
	}

	
}
