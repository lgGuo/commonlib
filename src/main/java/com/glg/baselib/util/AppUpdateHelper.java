package com.glg.baselib.util;


import com.glg.baselib.base.BaseApplication;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;

/**
 * app 检查更新帮助类
 */
public class AppUpdateHelper {


    /**
     * 下载app
     * @param url 下载地址
     * @param path 保存路径
     * @param forceUpdate 是否强制升级
     * @param onUpdateProgressListener
     */
    public static void downloadApp(String url,String path,boolean forceUpdate,OnUpdateProgressListener onUpdateProgressListener){





        FileDownloader.getImpl().create(url).setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void completed(BaseDownloadTask task) {

            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                Logger.e(e.getMessage());
                ToastUtil.show(BaseApplication.getMyApplication(),"下载失败");
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        }).start();
    }




    public interface  OnUpdateProgressListener{
        void  onUdateProgress(int soFarBytes, int totalBytes);
    }


}
