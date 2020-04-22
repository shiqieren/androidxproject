package com.liyiwei.basenetwork.filemanager.uploadstream;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;

import com.j256.ormlite.dao.Dao;
import com.sie.mp.app.BroadcastConstants;
import com.sie.mp.app.IMApplication;
import com.sie.mp.data.AttributeTypeConstants;
import com.sie.mp.data.MpFiles;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class UploadService extends Service {

	public class UploadBinder extends Binder {
		public UploadService getService() {
			return UploadService.this;
		}
	}

	// 网络监听，当网络恢复正常时就要恢复上传，当网络断掉之后就终止上传
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager;
			NetworkInfo info;
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {// 如果网络可用, 就继续上传
					UploadService.this.loadPendingFiles();
				} else {
					UploadService.this.touploadQueue.clear();
				}
			}else if(action.equals(BroadcastConstants.BROADCAST_RESUME_UPLOAD)){
				connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {// 如果网络可用, 就继续上传
					UploadService.this.loadPendingFiles();
				} else {
					UploadService.this.touploadQueue.clear();
				}
			}
		}
	};

	// 待上传队列
	PriorityBlockingQueue<MpFiles> touploadQueue = new PriorityBlockingQueue<MpFiles>();
	UploadConsumer consumer;

	// 待完成的队列
	ArrayList<MpFiles> toCompletes = new ArrayList<MpFiles>();

	private final IBinder binder = new UploadBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// 初始化
		init();

		// 注册网络状态监听器
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mFilter.addAction(BroadcastConstants.BROADCAST_RESUME_UPLOAD);
		registerReceiver(mReceiver, mFilter);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(this.mReceiver);
	}

	/**
	 * 加载没有传完的文件，并放到线程队列中
	 * 
	 * @throws SQLException
	 */
	private void loadPendingFiles() {

		List<MpFiles> pendingFiles;
		try {
			pendingFiles = UploadUtils.queryInitPendingFiles(null, null, null,"Y");
			if (pendingFiles != null) {
				for (int i = 0; i < pendingFiles.size(); i++) {
					this.touploadQueue.add(pendingFiles.get(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化，并创建队列监听器
	 */
	private void init() {
		this.loadPendingFiles();

		consumer = new UploadConsumer(this);
		consumer.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
		consumer.start();
	}

	/**
	 * 要求File必须先创建成功
	 * 
	 * @param file
	 */
	public void addPendingFile(MpFiles file) {
		if(AttributeTypeConstants.FILE_UPLOAD_STATUS_Y.equals(file.getAutoUpload()))
		{
			this.touploadQueue.put(file);
		}
	}

	/**
	 * 添加待上传文件，files中所有的file必须先创建成功
	 * 
	 * @param files
	 */
	public void addPendingFiles(MpFiles[] files) {
		if (files != null) {
			for (MpFiles f : files) {
				addPendingFile(f);
			}
		}
	}

	/**
	 * 创建
	 * 
	 * @param files
	 */
	public void addPendingFiles(List<MpFiles> files) {
		if (files != null) {
			for (MpFiles f : files) {
				addPendingFile(f);
			}
		}
	}

	/**
	 * 添加待上传文件，并且文件还没有上传到服务器上
	 * 
	 * @param filePath
	 * @param sourceCode
	 * @param sourceId
	 * @param tagId
	 * @throws SQLException
	 */
	public void addPendingFile(String filePath, String sourceCode,
			long sourceId, String tagId, String autoUpload) {
		MpFiles file = new MpFiles();
		file.setFileId(System.currentTimeMillis());
		file.setClientId(file.getFileId());
		file.setFilePath(filePath);
		file.setSourceCode(sourceCode);
		file.setSourceId(sourceId);
		file.setTagId(tagId);
		file.setExceptionStatus("NONE");
		file.setUploadStatus("TO_ADD");
		File f = new File(filePath);
		file.setFileSize(f.length());
		file.setAutoUpload(autoUpload == null ? "Y" : autoUpload);
		file.setBlockCount(BlockReader.getBlockCount(f));
		file.setFileName(f.getName());

		// 添加到数据库;
		try {
			Dao dao = IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
			dao.create(file);
			addPendingFile(file);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addPendingFile(long fileId) {
		Dao dao;
		try {
			dao = IMApplication.getInstance().getDaoManager()
					.getDao(MpFiles.class);
			MpFiles f = (MpFiles) dao.queryForId(fileId);
			this.addPendingFile(f);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param filePath
	 * @param sourceCode
	 * @param sourceId
	 * @param tagId
	 */
	public void addPendingFiles(String[] filePath, String[] sourceCode,
			long[] sourceId, String[] tagId, String autoUpload) {
		if (filePath != null) {
			for (int i = 0; i < filePath.length; i++) {
				addPendingFile(filePath[i], sourceCode[i], sourceId[i],
						tagId[i], autoUpload);
			}
		}
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟侥硷拷
	 * 
	 * @param filePath
	 * @param sourceCode
	 * @param sourceId
	 * @param tagId
	 */
	public void addPendingFiles(String[] filePath, String sourceCode,
			long sourceId, String tagId, String autoUpload) {
		if (filePath != null) {
			for (int i = 0; i < filePath.length; i++) {
				addPendingFile(filePath[i], sourceCode, sourceId, tagId,
						autoUpload);
			}
		}
	}

}
