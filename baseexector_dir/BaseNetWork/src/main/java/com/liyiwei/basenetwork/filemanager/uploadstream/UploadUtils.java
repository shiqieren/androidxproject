package com.liyiwei.basenetwork.filemanager.uploadstream;

/**
 * 辅助类，注意这些方法都是同步方法
 */

import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.sie.mp.app.IMApplication;
import com.sie.mp.app.URLConstants;
import com.sie.mp.data.MpFiles;
import com.sie.mp.util.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

public class UploadUtils {

	private static List<MpFiles> queryFilesByStatus(String status)
			throws SQLException {
		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);

		QueryBuilder builder = dao.queryBuilder();

		builder.where().eq("UPLOAD_STATUS", status);

		return builder.query();
	}

	/**
	 * 查询待新增的客户端文件
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static List<MpFiles> queryToAddClientFiles() throws SQLException {
		return queryFilesByStatus(UploadConstants.UPLOAD_STATUS_TO_ADD);
	}

	/**
	 * 
	 * @param sourceCode
	 * @param sourceId
	 * @param tagId
	 * @param autoLoad
	 * @return
	 * @throws SQLException
	 */
	public static List<MpFiles> queryInitPendingFiles(String sourceCode,
			String sourceId, String tagId, String autoLoad) throws SQLException {
		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);

		QueryBuilder builder = dao.queryBuilder();

		Where where = builder.where();

		where.ne("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_COMPLETE);
		where.and().ne("UPLOAD_STATUS",UploadConstants.UPLOAD_STATUS_EXCEPTION);

		if (sourceCode != null) {
			where.and().eq("SOURCE_CODE", sourceCode);
		}

		if (sourceId != null) {
			where.and().eq("SOURCE_ID", sourceId);
		}

		if (autoLoad != null) {
			where.and().eq("AUTO_UPLOAD", autoLoad);
		}

		if (tagId != null) {
			where.and().eq("TAG_ID", tagId);
		}

		return builder.query();
	}

	/**
	 * 查询上传有错误的文件
	 * 
	 * @param sourceCode
	 * @param sourceId
	 * @param tagId
	 * @return
	 * @throws SQLException
	 */
	public static List<MpFiles> queryFailFiles(String sourceCode,
			String sourceId, String tagId, String autoLoad) throws SQLException {
		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);

		QueryBuilder builder = dao.queryBuilder();

		Where where = builder.where();

		where.eq("EXCEPTION_STATUS", "EXCEPTION");

		if (sourceCode != null) {
			where.and().eq("SOURCE_CODE", sourceCode);
		}

		if (sourceId != null) {
			where.and().eq("SOURCE_ID", sourceId);
		}

		if (autoLoad != null) {
			where.and().eq("AUTO_UPLOAD", autoLoad);
		}

		if (tagId != null) {
			where.and().eq("TAG_ID", tagId);
		}

		return builder.query();
	}

	/**
	 * 通过来源查询文件
	 * 
	 * @param sourceCode
	 * @param sourceId
	 * @return
	 * @throws SQLException
	 */
	public static List<MpFiles> queryClientFilesBySource(String sourceCode,
			String sourceId, String status) throws SQLException {
		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);

		QueryBuilder builder = dao.queryBuilder();
		Where where = builder.where();
		where.eq("SOURCE_CODE", sourceCode).and().eq("SOURCE_ID", sourceId);
		if (status != null) {
			where.and().eq("UPLOAD_STATUS", status);
		}

		return builder.query();
	}

	/**
	 * 通过来源编码+标记查询文件
	 * 
	 * @param sourceCode
	 * @param tagId
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	public static List<MpFiles> queryClientFileByTag(String sourceCode,
			String tagId, String status) throws SQLException {
		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);

		QueryBuilder builder = dao.queryBuilder();
		Where where = builder.where();
		where.eq("SOURCE_CODE", sourceCode).and().eq("TAG_ID", tagId);
		if (status != null) {
			where.and().eq("UPLOAD_STATUS", status);
		}

		return builder.query();
	}

	/**
	 * 加载等待上传的文件
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static List<MpFiles> queryPendingUploadFiles(String sourceCode,
			String tagId, String sourceId) throws SQLException {

		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);
		QueryBuilder builder = dao.queryBuilder();
		Where where = builder.where();
		where.eq("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_TO_UPLOAD)
				.and().eq("EXCEPTION_STATUS", "NONE");

		if (sourceCode != null) {
			where.and().eq("SOURCE_CODE", sourceCode);
		}

		if (sourceId != null) {
			where.and().eq("SOURCE_ID", sourceId);
		}

		if (tagId != null) {
			where.and().eq("TAG_ID", tagId);
		}

		return builder.query();
	}

	public static List<MpFiles> queryErrorFiles(String sourceCode,
			String tagId, String sourceId) throws SQLException {
		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);
		QueryBuilder builder = dao.queryBuilder();
		Where where = builder.where();
		where.eq("EXCEPTION_STATUS", "EXCEPTION");

		if (sourceCode != null) {
			where.and().eq("SOURCE_CODE", sourceCode);
		}

		if (sourceId != null) {
			where.and().eq("SOURCE_ID", sourceId);
		}

		if (tagId != null) {
			where.and().eq("TAG_ID", tagId);
		}

		return builder.query();
	}

	public static MpFiles getFileById(long fileId) throws SQLException {
		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);
		return (MpFiles) dao.queryForId(fileId);
	}

	public static long getCompleteFileCount() throws SQLException {
		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);

		QueryBuilder builder = dao.queryBuilder();

		builder.where()
				.eq("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_TO_COMPLETE)
				.or()
				.eq("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_COMPLETE);
		return builder.countOf();
	}

	public static List<MpFiles> queryCompleteFiles(int begin, int length)
			throws SQLException {
		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);

		QueryBuilder builder = dao.queryBuilder();

		builder.where()
				.eq("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_TO_COMPLETE)
				.or()
				.eq("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_COMPLETE);
		builder.limit(Long.valueOf(length));
		builder.offset(Long.valueOf(begin));
		builder.orderBy("COMPLETE_DATE", false);
		return builder.query();
	}

	/**
	 * 在服务器申请创建空白文件，注意：是同步请求
	 * 
	 * @param files
	 * @return
	 * @throws SQLException
	 */
	public static List<MpFiles> addBlankClientFiles(List<MpFiles> files)
			throws SQLException {
		if (!NetworkUtils.checkNet(IMApplication.getInstance()
				.getApplicationContext())) {
			return null;
		}

		// 没有添加到数据库的文件先添加到数据库
		Dao dao = IMApplication.getInstance().getDaoManager()
				.getDao(MpFiles.class);
		int len = files.size();
		for (int i = 0; i < len; i++) {
			MpFiles f = files.get(i);
			if (f.getFileId() == 0) {// 如果文件ID为0，表示还没有创建本地数据库文件
				f.setFileId(System.currentTimeMillis());
				f.setClientId(f.getFileId());
				dao.create(f);
			}
		}

		try {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
					.create();
			JSONObject content = new JSONObject();
			JSONArray params = new JSONArray(gson.toJson(files));
			content.put("clientFile", params);
			RequestFuture<JSONObject> future = RequestFuture.newFuture();

			JsonObjectRequest req = new JsonObjectRequest(Method.POST,
					URLConstants.REMOTE_HOST
							+ UploadConstants.ADD_CLIENT_MULIT_FILE_ACTION,
					content, future, future);
			IMApplication.getInstance().getHttpManager().add(req);
			JSONObject response = future.get();
			if (response != null && response.has("clientFile")) {
				JSONArray arr = response.getJSONArray("clientFile");
				List<MpFiles> fs = gson.fromJson(arr.toString(),
						new TypeToken<List<MpFiles>>() {
						}.getType());
				for (int i = 0; i < fs.size(); i++) {
					UpdateBuilder builder = dao.updateBuilder();
					builder.updateColumnValue("FILE_ID", fs.get(i).getFileId());
					builder.updateColumnValue("UPLOAD_STATUS",
							UploadConstants.UPLOAD_STATUS_TO_UPLOAD);
					builder.where().eq("FILE_ID", fs.get(i).getClientId());
					builder.update();
				}

				return fs;
			}

			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private static final String[] IMAGE_TYPES_ARRAY = { "JPG", "JPEG", "BMP",
			"PNG" };

	public static boolean isImageFile(String file) {
		String ext = "";
		if (file != null) {
			int typeIndex = file.lastIndexOf(".");
			if (typeIndex != -1) {
				ext = file.substring(typeIndex + 1).toLowerCase();
			}

			for (int i = 0; i < IMAGE_TYPES_ARRAY.length; i++) {
				if (IMAGE_TYPES_ARRAY[i].equalsIgnoreCase(ext)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	public static String parseFileSize(long size) {

		if (size >= 1024 * 1024) {// 如果已经是M
			return Math.round((size * 100.00 / (1024.00 * 1024.00))) * 1.00
					/ 100 + "M";
		} else if (size >= 100) {
			return Math.round((size * 100.00 / (1024.00))) * 1.00 / 100 + "K";
		} else {
			return size + "B";
		}

	}
}
