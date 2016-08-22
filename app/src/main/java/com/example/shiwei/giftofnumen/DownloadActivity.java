package com.example.shiwei.giftofnumen;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadActivity extends AppCompatActivity implements HttpCallBack{

    private Object id;
    private ImageButton imagebutton;
    private ImageView imageview;
    private TextView title;
    private TextView title1;
    private List<DownLoaddetailProductInfo> productInfoList=new ArrayList<>();
    private List<DownLoaddetailProductInfo.ImgBean> productInfoimagelist=new ArrayList<>();
    private TextView size;
    private RecyclerView recycleview;
    private TextView content;
    private TextView type;
    private Button button;
    private int contentLength;
    private int length;
    private int filesize;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                if (filesize>=0&&filesize<contentLength){
                    progressDialog.setMax(contentLength);
                    progressDialog.setProgress(filesize);
                    progressDialog.setMessage("正在下载中");
                    progressDialog.show();
                }else if (filesize==contentLength){
                    progressDialog.dismiss();
                    AlertDialog.Builder build=new AlertDialog.Builder(DownloadActivity.this)
                            .setMessage("下载完成，是否安装?").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////filePath为文件路径
//                                    intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.packagearchive");
//                                    startActivity(intent);
                                }
                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    build.show();
                }
            }
        }
    };
    private DownLoaddetailProductInfo downLoaddetailProductInfo;
    private ProgressDialog progressDialog;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Intent intent = getIntent();
       id = intent.getExtras().get("id");

        //初始化视图
        initView();
        //请求网络
        getHttp();
    }

    private void getHttp() {
        Map<String,Object> map=new HashMap<>();
        map.put("id", id);
        HttpUtils.postHttp(DownLoadadUrl.commomurl, this, 0, map);
    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        imagebutton=(ImageButton)findViewById(R.id.download_detail_imagebtn);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title=(TextView)findViewById(R.id.download_detail_title1);
        title1=(TextView)findViewById(R.id.download_detail_title2);
        size=(TextView)findViewById(R.id.download_detail_appsize);
        type=(TextView)findViewById(R.id.download_detail_type);

        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        linearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleview=(RecyclerView)findViewById(R.id.download_detail_recycleview);
        recycleview.setLayoutManager(linearLayout);
        recycleview.setHasFixedSize(true);


        imageview=(ImageView)findViewById(R.id.download_detail_imageicon);
        content=(TextView)findViewById(R.id.download_detail_youxicontent);
        button=(Button)findViewById(R.id.download_detail_btn);

    }

    private void download() {
       new Thread(new Runnable() {
            @Override
            public void run() {
                filesize=0;
                contentLength=0;
                String s = Environment.getExternalStorageDirectory() + "/LiBaoGameDownload/";
                file=new File(s+downLoaddetailProductInfo.getApp().getName());
                //如果文件夹不存在则创建
                if (!file.exists()){
                    file.mkdir();
                }
                try {
                    URL url=new URL(downLoaddetailProductInfo.getApp().getDownload_addr());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    if (urlConnection.getResponseCode()==200){
                        length = urlConnection.getContentLength();
                        contentLength=length;
                        InputStream inputStream = urlConnection.getInputStream();
                        OutputStream outputStream=new FileOutputStream(file+".apk");
                        byte[] bytes=new byte[1024];
                        int len=0;
                        while ((len=inputStream.read(bytes))!=-1){
                            outputStream.write(bytes,0,len);
                            filesize+=inputStream.read(bytes);
                            handler.sendEmptyMessage(0);
                        }
                        inputStream.close();
                        outputStream.close();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onsuccess(String result, int requestcode) {
        Gson gson=new Gson();
        downLoaddetailProductInfo = gson.fromJson(result, DownLoaddetailProductInfo.class);
        for (int i=0;i<downLoaddetailProductInfo.getImg().size();i++){
            productInfoimagelist.add(downLoaddetailProductInfo.getImg().get(i));
        }
        Picasso.with(this).load("http://www.1688wan.com"+downLoaddetailProductInfo.getApp().getLogo()).into(imageview);
        title.setText(downLoaddetailProductInfo.getApp().getName());
        title1.setText(downLoaddetailProductInfo.getApp().getName());
        type.setText("类型:"+downLoaddetailProductInfo.getApp().getTypename());
        if (downLoaddetailProductInfo.getApp().getAppsize().isEmpty()&&downLoaddetailProductInfo.getApp().getDownload_addr().isEmpty()){
            size.setText("大小:未知");
            button.setText("暂无下载");
            button.setBackgroundResource(R.drawable.taojin);
        }else {
            size.setText("大小:"+downLoaddetailProductInfo.getApp().getAppsize());
            button.setText("立即下载");
            button.setBackgroundResource(R.drawable.lingqurigthnow);
            // 下载按钮
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //下载
                    download();
                }
            });
        }
        content.setText(downLoaddetailProductInfo.getApp().getDescription());
        //设置recycle
        DownLoadRecycleViewAapter adapter=new DownLoadRecycleViewAapter();
        recycleview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    class DownLoadRecycleViewAapter extends RecyclerView.Adapter<DownLoadRecycleViewAapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(DownloadActivity.this).inflate(R.layout.download_recycleview_inflater,null);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Picasso.with(DownloadActivity.this).load("http://www.1688wan.com" + productInfoimagelist.get(position).getAddress()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return productInfoimagelist.size();
        }

        public  class MyViewHolder extends RecyclerView.ViewHolder{
            private ImageView imageView;
            public MyViewHolder(View itemView) {
                super(itemView);
                imageView=(ImageView)itemView.findViewById(R.id.download_detail_recycleview_inflater_imageview);
            }
        }
    }
}
