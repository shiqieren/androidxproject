package com.liyiwei.basenetwork.filemanager.uploadform;

public interface ProgressListener {

    void onProgress(long currentBytes, long contentLength, boolean done);

}