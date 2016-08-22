package com.example.shiwei.giftofnumen.spice.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiwei.giftofnumen.DownloadActivity;
import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.customerview.CustomGridView;
import com.example.shiwei.giftofnumen.spice.fragments.bean.SpiceThirdDetailProductInfo;
import com.example.shiwei.giftofnumen.spice.fragments.httpadress.SpiceAdress;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpiceActivityDetail extends AppCompatActivity implements HttpCallBack {
    private int id;
    private ImageButton thirddetailbtn;
    private ImageButton thirddetailshare;
    private ImageView thirddetailimageview;
    private TextView thirdetailcontent;
    private TextView thirdetailtitle;
    private CustomGridView thirddetailgridview;
    private Map<String,Object> map=new HashMap<>();
    private List<SpiceThirdDetailProductInfo.ListBean> listBeanlist=new ArrayList<>();
    private String title;
    private String iconurl;
    private String content;
    private SpiceThirdDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spice_activity_detail);
        Bundle bundle = getIntent().getExtras();
         id= bundle.getInt("id");
        title=bundle.getString("title");
        iconurl=bundle.getString("iconurl");
        content=bundle.getString("content");
        //初始化视图
        initView();
        //请求网络
        getHttp();
    }

    private void getHttp() {
        map.put("id",id);
        HttpUtils.postHttp(SpiceAdress.spice_third_detail,this,0,map);
    }

    private void initView() {
        thirddetailbtn=(ImageButton)findViewById(R.id.spice_third_detail_btn);
        thirddetailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpiceActivityDetail.this.finish();
            }
        });
        thirddetailshare=(ImageButton)findViewById(R.id.spice_third_detail_share);

        thirddetailimageview=(ImageView)findViewById(R.id.spice_third_detail_image);
        Picasso.with(this).load("http://www.1688wan.com"+iconurl).into(thirddetailimageview);

        thirdetailcontent=(TextView)findViewById(R.id.spice_third_detail_daodu);
        thirdetailcontent.setText("导读:" + content);

        thirdetailtitle=(TextView)findViewById(R.id.spice_third_detail_title);
        thirdetailtitle.setText(title);
        thirddetailgridview=(CustomGridView)findViewById(R.id.spice_third_detail_gridview);
    }

    @Override
    public void onsuccess(String result, int requestcode) {
        Gson gson=new Gson();
        SpiceThirdDetailProductInfo spiceThirdDetailProductInfo = gson.fromJson(result, SpiceThirdDetailProductInfo.class);
        for (int i=0;i<spiceThirdDetailProductInfo.getList().size();i++){
            listBeanlist.add(spiceThirdDetailProductInfo.getList().get(i));
        }
        adapter=new SpiceThirdDetailAdapter();
        thirddetailgridview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    class SpiceThirdDetailAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listBeanlist.size();
        }

        @Override
        public Object getItem(int i) {
            return listBeanlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            SpicedetailViewholder spicedetailViewholder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.spice_third_detail_gridview,null);
                spicedetailViewholder=new SpicedetailViewholder();
                spicedetailViewholder.button=(Button)view.findViewById(R.id.spice_third_detail_inflater_Button);
                spicedetailViewholder.imageView=(ImageView)view.findViewById(R.id.spice_third_detail_inflater_image);
                spicedetailViewholder.content=(TextView)view.findViewById(R.id.spice_third_detail_inflater_title);
                view.setTag(spicedetailViewholder);
            }else {
                spicedetailViewholder=(SpicedetailViewholder)view.getTag();
            }
            Picasso.with(getApplicationContext()).load("http://www.1688wan.com"+listBeanlist.get(i).getAppicon()).into(spicedetailViewholder.imageView);
            spicedetailViewholder.content.setText(listBeanlist.get(i).getAppname());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(SpiceActivityDetail.this, DownloadActivity.class);
                    intent.putExtra("id",listBeanlist.get(i).getAppid());
                    startActivity(intent);
                }
            });
            return view;
        }
    }
   class SpicedetailViewholder{
       private ImageView imageView;
       private Button button;
       private TextView content;
   }
}
