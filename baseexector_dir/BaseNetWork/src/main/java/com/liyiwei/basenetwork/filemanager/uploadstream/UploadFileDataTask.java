package com.liyiwei.basenetwork.filemanager.uploadstream;

public class UploadFileDataTask  extends AsyncTask<Void, Integer, Boolean> {
    private static final String TAG = "UploadFileDataTask";
    private Context mContext;
    long mTtotalSize ;
    File mFile;
    String destPath;
    String access_token;
    String filename;
    public UploadFileDataTask(Context mContext, File file, String uploadPath, String token, String filename) {
        this.mContext = mContext;
        this.mFile = file;
        this.destPath = uploadPath;
        this.access_token = token;
        this.filename = filename;
        mTtotalSize = file.length();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        long length = 0;
        int mBytesRead, mbytesAvailable, mBufferSize;
        int maxBufferSize = 8192;
        if (!mFile.exists()) {
            publishProgress(0);
            return true;
        }
        VLog.i(TAG,"start upload");
        Map<String, Object> mPostMap = new HashMap<String, Object>();
        mPostMap.put("path", destPath); // 目标路径
        mPostMap.put("filelen", mFile.length()); // 文件长度
        URL url = null;
        HttpURLConnection conn =null;
        BufferedOutputStream out = null;
        try {
            url = new URL("http://172.16.96.30/fs/v1/upload/");// 目标服务器上传接口
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod(AuthenticationUtil.HTTP_METHOD_POST);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-type", "multipart/form-data;boundary=" + AuthenticationUtil.BOUNDARYSTR);
            conn.addRequestProperty("cookie", "access_token=" + access_token);
            conn.connect();

            out = new BufferedOutputStream(conn.getOutputStream());
            StringBuilder sb = new StringBuilder();

            Iterator<String> it = mPostMap.keySet().iterator();
            while (it.hasNext()) {
                String str = it.next();
                sb.append(AuthenticationUtil.BOUNDARY);
                sb.append("Content-Disposition:form-data;name=\"");
                sb.append(str);
                sb.append("\"\r\n\r\n");
                sb.append(mPostMap.get(str));
                sb.append("\r\n");
            }

            // post the string data.
            out.write(sb.toString().getBytes());
            out.write(AuthenticationUtil.BOUNDARY.getBytes());

            StringBuilder filenamesb = new StringBuilder();
            filenamesb.append("Content-Disposition:form-data;Content-Type:application/octet-stream;name=\"uploadfile");
            filenamesb.append("\";filename=\"");
            filenamesb.append(mFile.getName() + "\"\r\n\r\n");
            out.write(filenamesb.toString().getBytes());

            FileInputStream fis = new FileInputStream(mFile);
            mbytesAvailable = fis.available();
            mBufferSize = Math.min(mbytesAvailable, maxBufferSize);
            byte[] buffer = new byte[mBufferSize]; // 8k
            mBytesRead = fis.read(buffer, 0, mBufferSize);

            // 读取文件
            while (mBytesRead > 0) {
                out.write(buffer, 0, mBufferSize);
                length += mBufferSize;
                publishProgress((int) ((length * 100) / mTtotalSize));

                mbytesAvailable = fis.available();

                mBufferSize = Math.min(mbytesAvailable, maxBufferSize);

                mBytesRead = fis.read(buffer, 0, mBufferSize);
            }

            out.write("\r\n".getBytes());
            out.write(("--" + AuthenticationUtil.BOUNDARYSTR + "--\r\n").getBytes());
            publishProgress(100);
            fis.close();
        } catch (IOException e) {
            VLog.i(TAG,"upload IOException");
            e.printStackTrace();
            AuthenticationUtil.analysisHttpStatus(conn);
        } catch (Exception e) {
            VLog.i(TAG,"upload Exception");
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    VLog.i(TAG,"ResponseCode"+ conn.getResponseCode());
                    VLog.i(TAG,"ResponseMessage"+ conn.getResponseMessage());
                    if (conn.getResponseCode() == 200){
                        return true;
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

        }
//        AuthenticationUtil.upload(file,"/vivo/testupload",token,filename);
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