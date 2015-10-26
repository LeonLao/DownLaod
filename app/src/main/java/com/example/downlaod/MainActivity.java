package com.example.downlaod;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;


import org.apache.http.Header;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends Activity {
    Button btncheck,btnstart,btnstop,btnlist;
    private WifiManager wifiManager;
    String  url ;
    EditText editText ;
    ListView listView;
    private File audiofile;
    private File audiopath;
    private File audiopath2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //检查有无SD卡卡卡卡卡卡卡卡啊卡卡卡卡卡卡啊阿卡阿卡卡啊卡卡卡啊
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)){
            //呢个系获取个个SD卡嘅路径
            audiopath =Environment.getExternalStorageDirectory();
        }else {
            //无SD卡就用自带存储路径audiopath2
            audiopath2 =Environment.getRootDirectory();
            Toast.makeText(MainActivity.this,"检查到手机没有SD卡喔",Toast.LENGTH_LONG).show();
        }

        btncheck = (Button)findViewById(R.id.button);
        btncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取wifi管理服务
                wifiManager = (WifiManager)MainActivity.this.getSystemService(Context.WIFI_SERVICE);
                System.out.print("wifi state ----->"+wifiManager.getWifiState());
                Toast.makeText(MainActivity.this,"返回1代表wifi关闭，3表示连接上，可用,result:"+wifiManager.getWifiState(),Toast.LENGTH_LONG).show();
            }
        });
        editText = (EditText)findViewById(R.id.editText);
        url = editText.getText().toString();

        btnstart = (Button)findViewById(R.id.button2);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    downloadFile(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnlist =(Button)findViewById(R.id.button4);
        btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList();
            }
        });


    }
    //显示文件列表
    private void showList() {
        File home = audiopath;
    }

    public static void downloadFile(String url) throws Exception{
       // HttpUtil.get(url, new BinaryHttpResponseHandler() {
        AsyncHttpClient client  = new AsyncHttpClient();
        String[] allowedContentTypes = new String[] { "image/png", "image/jpeg" };
        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                String tempPath  = Environment.getExternalStorageDirectory().getPath()+"/kkkkk.jpg";
                Bitmap bmp = BitmapFactory.decodeByteArray(binaryData,0,binaryData.length);
                File file = new File(tempPath);
                //压缩格式
                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                //压缩比例
                int quality = 100;
                try {
                    //若存在则删除
                    if (file.exists())
                        file.delete();
                    //创建文件
                    file.createNewFile();
                    OutputStream stream = new FileOutputStream(file);
                    //压缩输出
                    bmp.compress(format,quality,stream);
                    //关闭流
                    stream.close();
                   // Toast.makeText(mContext,"下载完成",Toast.LENGTH_LONG).show();
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                 //   Toast.makeText(MainActivity.this,"ss",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int count = (int)(bytesWritten*1.0/totalSize*100);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }
        });
    }






}
