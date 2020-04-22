package com.liyiwei.basenetwork.filemanager.download;

//import com.j256.ormlite.dao.Dao;
//import com.sie.mp.app.IMApplication;
//import com.sie.mp.data.FileDownLog;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileService 
{
	/**
	 * 获取每条线程已经下载的文件长度
	 * @param path
	 * @return
	 */
	public static Map<Integer, Integer> getData(String path)
	{
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
//		try
//		{
//			Dao dao=IMApplication.getInstance().getDaoManager().getDao(FileDownLog.class);
//			List<FileDownLog> files = dao.queryBuilder().where().eq("downpath", path).query();
//			for(int i=0;i<files.size();i++)
//				data.put(files.get(i).getThreadid(), files.get(i).getDownlength());
//		}
//		catch (SQLException e){}
		return data;
	}

	/**
	 * 保存每条线程已经下载的文件长度
	 * @param path
	 * @param map
	 */
	public static void save(String path,  Map<Integer, Integer> map)
	{
//		try
//		{
//			Dao dao=IMApplication.getInstance().getDaoManager().getDao(FileDownLog.class);
//			for(Map.Entry<Integer, Integer> entry : map.entrySet())
//			{
//				FileDownLog file=(FileDownLog)dao.queryBuilder().where().eq("downpath", path).and().eq("threadid", entry.getKey()).queryForFirst();
//				if(file==null)
//				{
//					file=new FileDownLog();
//				}
//				//FileDownLog file=new FileDownLog();
//				//file.setId(0l);
//				file.setDownpath(path);
//				file.setThreadid(entry.getKey());
//				file.setDownlength(entry.getValue());
//				dao.createOrUpdate(file);
//			}
//		}
//		catch (SQLException e){
//
//			int z=0;
//		}
	}

	/**
	 * 实时更新每条线程已经下载的文件长度
	 * @param path
	 * @param map
	 */
	public static void update(String path, Map<Integer, Integer> map)
	{
//		try
//		{
//			Dao dao=IMApplication.getInstance().getDaoManager().getDao(FileDownLog.class);
//			for(Map.Entry<Integer, Integer> entry : map.entrySet())
//			{
//				FileDownLog file=(FileDownLog)dao.queryBuilder().where().eq("downpath", path).and().eq("threadid", entry.getKey()).queryForFirst();
//				if(file!=null)
//				{
//					file.setDownlength(entry.getValue());
//					dao.createOrUpdate(file);
//				}
//			}
//		}
//		catch (SQLException e){}
	}

	/**
	 * 当文件下载完成后，删除对应的下载记录
	 * @param path
	 */
	public static void delete(String path)
	{
//		try
//		{
//			Dao dao=IMApplication.getInstance().getDaoManager().getDao(FileDownLog.class);
//			List<FileDownLog> files = dao.queryBuilder().where().eq("downpath", path).query();
//			for(int i=files.size()-1;i>=0;i--)
//				dao.delete(files.get(i));
////			 ModComm.MP_DB.execSQL("delete from filedownlog where downpath=?", new Object[]{path});
//		}
//		catch (SQLException e){}
	}
}
