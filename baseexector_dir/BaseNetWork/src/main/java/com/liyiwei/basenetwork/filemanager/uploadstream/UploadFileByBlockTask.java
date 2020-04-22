package com.liyiwei.basenetwork.filemanager.uploadstream;

import android.content.Context;
import android.os.AsyncTask;

public class UploadFileByBlockTask extends AsyncTask<Void, Integer, Boolean> {
private static final String TAG = "UploadFileByBlockTask";
private Context mContext;
        long mTtotalSize ;
        File mFile;
        String destPath;
        String access_token;
        String filename;
        String uploadId;
public UploadFileByBlockTask(Context mContext, File file, String uploadPath, String token, String filename,String uploadId) {
        this.mContext = mContext;
        this.mFile = file;
        this.destPath = uploadPath;
        this.access_token = token;
        this.filename = filename;
        this.uploadId = uploadId;
        mTtotalSize = file.length();
        }

@Override
protected Boolean doInBackground(Void... params) {
        if (!mFile.exists()) {
        VLog.i(TAG,"file not exists");
        publishProgress(0);
        return false;
        }
        // UPLOAD_BLOCK_SIZE 必须是 UPLOAD_BUFFER_SIZE 的倍数， 方便计算
        if((UploadByBlock.UPLOAD_BLOCK_SIZE % UploadByBlock.UPLOAD_BUFFER_SIZE) != 0) {
        VLog.i(TAG,"UPLOAD_BLOCK_SIZE should be a multiple of UPLOAD_BUFFER_SIZE");
        return false;
        }
        VLog.i(TAG,"start upload");
        String fileName = mFile.getName();
        String md5 = UploadByBlock.getMd5ByFile(mFile);
        long fileSize = mFile.length();

        // 获取当前文件的上传进度
        JSONObject uploadInfo = null;
        // 已经上传的大小
        long uploadedSize = 0;
        try {
        uploadInfo = UploadByBlock.getContInfo(destPath, fileName, md5, uploadId, fileSize,access_token);
        if(uploadInfo == null || uploadInfo.getString("uploadId") == null) {
        VLog.i(TAG,"error");
        return false;
        }

        // 代表文件上传进度的id
        uploadId = (String) uploadInfo.getString("uploadId");
        uploadedSize = Long.valueOf(uploadInfo.getString("size").toString());
        } catch (Exception e) {
        e.printStackTrace();
        }

        VLog.i(TAG,"uploadId : " + uploadId);
        VLog.i(TAG,"uploadedSize : " + uploadedSize);

        // 服务器已经存在此文件
        if(uploadedSize == fileSize) {
        VLog.i(TAG,"服务器已经存在此文件，可以秒传");
        UploadByBlock.completeUpload(md5, uploadId, destPath + fileName + "/",access_token);
        publishProgress(100);
        return false;
        } else { // 开始分块上传
        try {
        while(true) {
        int httpStatus = UploadByBlock.uploadBlock(mFile, uploadedSize, fileSize, UploadByBlock.UPLOAD_BLOCK_SIZE, destPath, uploadId, md5,access_token);
        VLog.i(TAG,"uploadId:"+uploadId+"-----has uploadedSize:"+uploadedSize+"/all fileSize:"+fileSize+"/block datalen:"+UploadByBlock.UPLOAD_BLOCK_SIZE);
        if(httpStatus == -1) {
        VLog.i(TAG,"upload fail");
        publishProgress((int) ((uploadedSize * 100) / mTtotalSize));
        return false;
        } else {
        if(httpStatus == 206) {
        uploadedSize += UploadByBlock.UPLOAD_BLOCK_SIZE;
        publishProgress((int) ((uploadedSize * 100) / mTtotalSize));
        VLog.i(TAG,"upload block success");
        VLog.i(TAG,"uploadedSize : " + uploadedSize);
        } else if(httpStatus == 200) {
        VLog.i(TAG,"upload end");
        publishProgress(100);
        return true;
        }
        }
        }
        } catch (Exception e) {
        e.printStackTrace();
        }
        }
        return true;
        }

@Override
protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        if (mContext instanceof FileDownloadActivity){
        ((FileDownloadActivity) mContext).getDayPb().setProgress(progress[0]);
        }

        }

@Override
protected void onPostExecute(Boolean result) {
        if(result){
        T.showShort(mContext, "上传成功");
        if (mContext instanceof FileDownloadActivity){
        ((FileDownloadActivity) mContext).getPvTime().show();
        }

        }
        super.onPostExecute(result);
        }

        }