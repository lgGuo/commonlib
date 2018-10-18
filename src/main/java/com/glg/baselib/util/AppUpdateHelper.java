package com.glg.baselib.util;


import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.glg.baselib.R;
import com.glg.baselib.base.BaseApplication;
import com.glg.baselib.util.appupdate.BaseUpdate;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import java.io.File;

/**
 * app 检查更新帮助类
 */
public class AppUpdateHelper {


    public static void updateApp(FragmentActivity activity, BaseUpdate baseUpdate) {

        if (SystemUtil.getNetWorkType() == 2) {//wifi
            String storePath = baseUpdate.getStorePath();

            if (new File(storePath).exists()) {
                showDownloadDoneDialog(activity, storePath);
            } else {
                downloadApp(activity, false, baseUpdate);
            }


        } else {
            showUpdateDialog(activity, baseUpdate);
        }


    }

    private static void showUpdateDialog(final FragmentActivity activity, final BaseUpdate baseUpdate) {

        NiceDialog.init().setLayoutId(R.layout.dialog_app_update)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        TextView confirm = holder.getView(R.id.submit);

                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                downloadApp(activity, true, baseUpdate);
                            }
                        });
                    }
                })
                .setOutCancel(!baseUpdate.isForceUpdate())
                .setMargin(40)
                .show(activity.getSupportFragmentManager());

    }


    private static void showDownloadDoneDialog(final FragmentActivity activity, final String path) {

        NiceDialog.init().setLayoutId(R.layout.dialog_app_update)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        TextView confirm = holder.getView(R.id.submit);
                        TextView content = holder.getView(R.id.content);

                        content.setText("安装包已经准别好，赶紧免流量安装，体验更多惊喜功能吧！");

                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //安装app
                                dialog.dismiss();
                                SystemUtil.installApk(activity, new File(path));

                            }
                        });
                    }
                })
                .setOutCancel(false)
                .setMargin(40)
                .show(activity.getSupportFragmentManager())
        ;

    }


    private static void downloadApp(final FragmentActivity activity, final boolean fromUpdateTips, final BaseUpdate baseUpdate) {

        FileDownloader.getImpl().create(baseUpdate.getUrl())
                .setPath(baseUpdate.getStorePath())
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Logger.e("totalBytes:" + totalBytes + "/soFarBytes:" + soFarBytes);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {

                        if (fromUpdateTips) {
                            //安装app
                            SystemUtil.installApk(activity, new File(task.getPath()));

                        } else {
                            showDownloadDoneDialog(activity, baseUpdate.getStorePath());

                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Logger.e(e.getMessage());
                        ToastUtil.show(BaseApplication.getMyApplication(), "下载失败");
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();

    }


    public static void stopAllDownloadTask() {
        FileDownloader.getImpl().clearAllTaskData();
    }


    public interface OnUpdateProgressListener {
        void onUpdateProgress(int soFarBytes, int totalBytes);

        void onUpdateCompleted(BaseDownloadTask task);
    }


}
