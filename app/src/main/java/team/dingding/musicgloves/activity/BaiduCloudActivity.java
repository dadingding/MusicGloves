package team.dingding.musicgloves.activity;

/**
 * Created by Elega on 2014/7/17.
 */


import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Vector;


import com.baidu.pcs.BaiduPCSClient;
import com.baidu.pcs.BaiduPCSActionInfo;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.imp.MusicScore;

public class BaiduCloudActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baiducloud);
        mbOauth=getIntent().getStringExtra("mbOauth");
        mbUiThreadHandler = new Handler();

        upload = (ImageView)findViewById(R.id.btnBaiduUpload);
        delete = (ImageView)findViewById(R.id.btnBaiduRemove);
        download = (ImageView)findViewById(R.id.btnBaiduDownload);
        quit=(ImageView)findViewById(R.id.btnBaiduQuit);


        upload.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                btnUploadOnClick();
            }
        });

        delete.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                btnDeleteOnClick();
            }
        });

        download.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                btnDownloadOnClick();
            }
        });

        quit.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                btnQuitOnClick();
            }
        });

    }


    //下载事件
    private void btnDownloadOnClick() {
        final Context context = this;
        final ProgressDialog dialog = ProgressDialog.show(this,
                "正在连接云服务器", "请稍后...", true);

        dialog.show();

        if (null != mbOauth) {
            Thread workThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        final String[] filesname = listFile();
                        dialog.dismiss();

                        mbUiThreadHandler.post(new Runnable() {
                            public void run() {
                                new AlertDialog.Builder(context)
                                        .setTitle("请选择")
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .setSingleChoiceItems(filesname, 0,
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(final DialogInterface dialog_, int which) {
                                                        dialog.dismiss();
                                                        dialog_.dismiss();
                                                        dialog.show();
                                                        final int which2 = which;
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                tryDownload(filesname[which2]);
                                                                dialog.dismiss();
                                                            }
                                                        }).start();
                                                    }
                                                }
                                        )
                                        .setNegativeButton("取消", null)
                                        .show();
                            }
                        });
                    } catch (NetworkErrorException e) {
                        dialog.dismiss();
                        mbUiThreadHandler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "网络发生错误，连接失败,请检查您的网络连接", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }
                }
            });

            workThread.start();
        }


        return;

    }


    //与服务器连接尝试下载
    private void tryDownload(String fileName){

        BaiduPCSClient api = new BaiduPCSClient();
        api.setAccessToken(mbOauth);
        String source = mbRootPath + "/"+fileName;
        String target = getApplicationContext().getFilesDir().getAbsolutePath() + "/"+fileName;
        final BaiduPCSActionInfo.PCSSimplefiedResponse ret = api.downloadFile(source,target);
        mbUiThreadHandler.post(new Runnable(){
            public void run(){
                if (ret.errorCode==0)
                    Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "下载失败"+  ret.message, Toast.LENGTH_SHORT).show();
            }
        });


    }



    //上传事件
    private void btnUploadOnClick(){
        final Context context=this;
        //dialog.show();

        final String[] filesname= MusicScore.listMsFile(this.getFilesDir());
        new AlertDialog.Builder(this)
                .setTitle("请选择")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(filesname, 0,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog_, int which) {
                                dialog_.dismiss();
                                //dialog.show();
                                tryUpload(filesname[which]);
                            }
                        }
                )
                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialog.dismiss();
                    }
                })
                .show();
        return;




    }

    //与服务器连接尝试上传
    private void tryUpload(final String filename){
        final ProgressDialog dialog = ProgressDialog.show(this,
                "正在上传至云服务器", "请稍后...", true);


        if(null != mbOauth){

            Thread workThread = new Thread(new Runnable(){
                public void run() {
                    final String tmpFile = getApplicationContext().getFilesDir().getAbsolutePath() + "/" + filename;
                    final BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);
                    final BaiduPCSActionInfo.PCSFileInfoResponse response = api.uploadFile(tmpFile, mbRootPath + "/"+filename);
                    dialog.dismiss();
                    mbUiThreadHandler.post(new Runnable(){
                        public void run(){
                            if (response.status.errorCode==0)
                                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), "上传失败"+  response.status.message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }});

            workThread.start();
        }

    }
    //列出服务器端的所有文件
    private String[] listFile() throws NetworkErrorException{
        Vector<String> result=new Vector<String>();
        if (null != mbOauth) {
            BaiduPCSClient api = new BaiduPCSClient();
            api.setAccessToken(mbOauth);
            String path = mbRootPath;
            final BaiduPCSActionInfo.PCSListInfoResponse ret = api.list(path, "name", "asc");
            if (ret.list==null) throw new NetworkErrorException();
            for(int i=0;i<ret.list.size();++i){
                String name=ret.list.get(i).path;
                if (name.length()>4 && name.substring(name.length()-4).equals(".msc")){
                    name=name.replaceFirst(mbRootPath+"/","");
                    if (name.indexOf("/")==-1) {
                        result.addElement(name);
                        //Log.v("233", name);
                    }
                }

            }

        }
        return (String[])result.toArray(new String[0]);

    }

    //删除云端文件
    private boolean deleteCloudFile(final String fileName) {
        if (null != mbOauth) {
            BaiduPCSClient api = new BaiduPCSClient();
            api.setAccessToken(mbOauth);
            final BaiduPCSActionInfo.PCSSimplefiedResponse ret = api.deleteFile(mbRootPath + "/"+fileName);
            if (ret.errorCode==0) return true;
            else return false;
        }
        return false;
    }


    //判断文件是否存在
    private boolean existFile(final String fileName) {
        if (null != mbOauth) {
            BaiduPCSClient api = new BaiduPCSClient();
            api.setAccessToken(mbOauth);
            final BaiduPCSActionInfo.PCSListInfoResponse ret = api.search(mbRootPath, fileName, true);
            if (ret.list.size() > 0) return true;
            else return false;
        }
        return false;
    }






    //删除事件
    private void btnDeleteOnClick(){
        final Context context = this;
        final ProgressDialog dialog = ProgressDialog.show(this,
                "正在连接云服务器", "请稍后...", true);

        dialog.show();

        if (null != mbOauth) {
            Thread workThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        final String[] filesname = listFile();
                        dialog.dismiss();

                        mbUiThreadHandler.post(new Runnable() {
                            public void run() {
                                new AlertDialog.Builder(context)
                                        .setTitle("请选择")
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .setSingleChoiceItems(filesname, 0,
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(final DialogInterface dialog_, int which) {
                                                        dialog.dismiss();
                                                        dialog_.dismiss();
                                                        dialog.show();
                                                        final int which2 = which;
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                tryDelete(filesname[which2]);
                                                                dialog.dismiss();
                                                            }
                                                        }).start();
                                                    }
                                                }
                                        )
                                        .setNegativeButton("取消", null)
                                        .show();
                            }
                        });
                    } catch (NetworkErrorException e) {
                        dialog.dismiss();
                        mbUiThreadHandler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "网络发生错误，连接失败,请检查您的网络连接", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }
                }
            });

            workThread.start();
        }


        return;

    }

    //尝试从云端删除
    private void tryDelete(String fileName){
        BaiduPCSClient api = new BaiduPCSClient();
        api.setAccessToken(mbOauth);
        String source = mbRootPath + "/"+fileName;
        final BaiduPCSActionInfo.PCSSimplefiedResponse ret = api.deleteFile(source);
        mbUiThreadHandler.post(new Runnable(){
            public void run(){
                if (ret.errorCode==0)
                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "删除失败"+  ret.message, Toast.LENGTH_SHORT).show();
            }
        });

    }


    //退出事件
    private void btnQuitOnClick(){
        this.finish();
    }


    /**
     * Called by the system when the device configuration changes while your activity is running.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //don't reload the current page when the orientation is changed
        super.onConfigurationChanged(newConfig);
    }

    private String mbOauth = null;

    // the api key
    /*
     * mbApiKey should be your app_key, please instead of "your app_key"
     */
    private final static String mbApiKey = "L6g70tBRRIXLsY0Z3HwKqlRE"; //your app_key";

    // the default root folder
    /*
     * mbRootPath should be your app_path, please instead of "/apps/pcstest_oauth"
     */
    private final static String mbRootPath =  "/apps/pcstest_oauth";


    private ImageView upload = null;
    private ImageView delete = null;
    private ImageView download = null;
    private ImageView quit=null;

    // the handler
    private Handler mbUiThreadHandler = null;

}