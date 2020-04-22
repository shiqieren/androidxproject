package com.liyiwei.basenetwork.filemanager.uploadstream;

import com.sie.mp.data.MpFiles;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadConsumer extends Thread {

	public final static int MAX_POOL_SIZE = 5;

	private ExecutorService pool; // 上传线程的线程池
	private UploadService svr; // 上传服务

	private boolean pause; // 是否暂停

	public UploadConsumer(UploadService service) {
		pool = Executors.newFixedThreadPool(MAX_POOL_SIZE);
		this.svr = service;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	/**
	 * 运行后总是监听服务中的toUploadQueue，如果不为空就返回内给文件，如果为空，就阻塞等待
	 */
	@Override
	public void run() {
		while (true) {
			try {
				MpFiles file = svr.touploadQueue.take();
				if (file != null) {
					UploadRunable r = new UploadRunable(file);
					this.pool.execute(r);
				}
			} catch (Exception e) {
			}
		}
	}

}
