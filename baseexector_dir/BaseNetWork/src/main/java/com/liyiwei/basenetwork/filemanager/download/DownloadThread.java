package com.liyiwei.basenetwork.filemanager.download;

import android.util.Log;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载线程类
 * @author Administrator
 *
 */
public class DownloadThread extends Thread
{
	
	private static final String TAG = "DownloadThread";
	private File saveFile;
	private URL downUrl;
	private int block;
	
	/* 下载开始位置  */
	private int threadId = -1;	
	private int downLength;
	private boolean finish = false;
	private boolean stopDownload = false;
	private FileDownloader downloader;
	
	/**
	 * @param downloader:下载器
	 * @param downUrl:下载地址
	 * @param saveFile:下载路径
	 * 
	 */
	public DownloadThread(FileDownloader downloader, URL downUrl, File saveFile, int block, int downLength, int threadId) 
	{
		this.downUrl = downUrl;
		this.saveFile = saveFile;
		this.block = block;
		this.downloader = downloader;
		this.threadId = threadId;
		this.downLength = downLength;
	}
	
	@Override
	public void run()
	{
		if(downLength < block){//未下载完成
			InputStream inStream=null;
			RandomAccessFile threadfile=null;
			//使用Get方式下载
			HttpURLConnection http = null;
			try
			{
				http=(HttpURLConnection) downUrl.openConnection();
				http.setRequestProperty("Connection", "close");
				http.setConnectTimeout(3 * 1000);
				http.setReadTimeout(5*1000);
				http.setRequestMethod("GET");
				http.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
				http.setRequestProperty("Accept-Language", "zh-CN");
				http.setRequestProperty("Referer", downUrl.toString()); 
				http.setRequestProperty("Charset", "UTF-8");
				
				int startPos = block * (threadId - 1) + downLength;//开始位置
				int endPos = block * threadId -1;//结束位置
				http.setRequestProperty("Range", "bytes=" + startPos + "-"+ endPos);//设置获取实体数据的范围
				http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
//				http.setRequestProperty("Connection", "Keep-Alive");	
				http.connect();
				int responseCode=http.getResponseCode();
				
				inStream =new BufferedInputStream(http.getInputStream());
				byte[] buffer = new byte[4*1024];
				int offset = 0;
				print("Thread " + this.threadId + " start download from position "+ startPos);
				threadfile = new RandomAccessFile(this.saveFile, "rwd");
				threadfile.seek(startPos);
				//boolean continuDown=true;
				Log.v("---downloadvoice---", "---downloadvoice---startdown--"+downLength);
				while ((offset= inStream.read(buffer, 0, 4*1024)) != -1&&!stopDownload)
				{
					threadfile.write(buffer, 0, offset);
					downLength += offset;
					downloader.append(offset);
					//downloader.updateNotSave(this.threadId, downLength);
					if(downloader.getChatId()!=0)
					{
//						int flag=SharedPreferencesUtils.getInt(IMApplication.getInstance().getCurrentUser().getUserId() + "." + SharedPreferences_Parameter.MP_CHAT_STOP_DOWNLOAD+"."+downloader.getChatId(), 0);
//						if(flag==1)
//						{
//							//归位是否上传标记
//							//continuDown=false;
//							this.stopDownload=true;
//						}
					}

				}
				if(stopDownload)
				{
					downloader.update(this.threadId, downLength);
				}

				
//				threadfile.close();
//				inStream.close();
				
				print("Thread " + this.threadId + " download finish");
				this.finish = true;
				try{
				Stopdownload();
				}catch(Exception e){}
			} 
			catch (Exception e)
			{
				downloader.update(this.threadId, downLength);
				e.printStackTrace();
				Log.v("---downloadvoice---", "---downloadvoice---Exception--"+e.getMessage());
				this.downLength = -1;
				print("Thread "+ this.threadId+ ":"+ e);
//				if(this.saveFile.exists()){
//					this.saveFile.delete();
//				}
			}finally
			{
//				Log.w("---finally--downloadvoice---", "---finally--downloadvoice---");
				try {  
				if(inStream!=null)
				{
					try {
						inStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(threadfile!=null)
				{
					try {
						threadfile.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (http != null) {  
					http.disconnect();  
					http = null;  
                }  
				} catch (Exception ioe) {  
	                ioe.printStackTrace();  
	               // throw new RuntimeException("Release resource failed.");  
	            }  
				
			}
		}
	}
	
	/**
	 * 打印日志信息
	 * @param msg
	 */
	private static void print(String msg)
	{
		Log.i(TAG, msg);
	}
	
	/**
	 * 下载是否完成
	 * @return
	 */
	public boolean isFinish() 
	{
		return finish;
	}
	
	/**
	 * 已经下载的内容大小
	 * @return 如果返回值为-1,代表下载失败
	 */
	public long getDownLength() 
	{
		return downLength;
	}
	
	public void Stopdownload(){
		this.interrupt();
		this.destroy();
	}

	public boolean isStopDownload() {
		return stopDownload;
	}

	public void setStopDownload(boolean stopDownload) {
		this.stopDownload = stopDownload;
	}
	
	
	
}
