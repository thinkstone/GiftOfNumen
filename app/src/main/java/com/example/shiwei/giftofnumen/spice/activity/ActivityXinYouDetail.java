package com.example.shiwei.giftofnumen.spice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shiwei.giftofnumen.DownloadActivity;
import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.spice.fragments.bean.SpiceXinYouDetailProductInfo;
import com.example.shiwei.giftofnumen.spice.fragments.httpadress.SpiceAdress;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityXinYouDetail extends AppCompatActivity implements HttpCallBack{

    private int id;
    private String title;
    private String content;
    private String iconurl;
    private String newurl;
    private List<SpiceXinYouDetailProductInfo.ListBean> listBeans=new ArrayList<>();
    private TextView xinyoudetailcontent;
    private TextView xinyoudetailtitle;
    private CircleImageView xinyoudetailcircle;
    private ImageView xinyoudetailimageview;
    private ListView xinyoudetaillistview;
    private ImageButton xinyoudetailback;
    private SpiceXinyouDeatilAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xin_you_detail);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        title=bundle.getString("title");
        content=bundle.getString("content");
        iconurl=bundle.getString("iconurl");
        newurl=bundle.getString("newsurl");
        //初始化视图
        initView();
        //请求网络
        gethttp();
    }

    private void gethttp() {
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        HttpUtils.postHttp(SpiceAdress.spice_xinyou_detail,this,0,map);
    }

    private void initView() {
        xinyoudetailcontent=(TextView)findViewById(R.id.spice_xinyou_detail_content);
        xinyoudetailcontent.setText(content);

        xinyoudetailtitle=(TextView)findViewById(R.id.spice_xinyou_detail_title);
        xinyoudetailtitle.setText(title);

        xinyoudetailimageview=(ImageView)findViewById(R.id.spice_xinyou_detail_iamgeview);
        Picasso.with(this).load("http://www.1688wan.com"+iconurl).into(xinyoudetailimageview);

        xinyoudetailcircle=(CircleImageView)findViewById(R.id.spice_xinyou_detail_circleiamgeview);
        Picasso.with(this).load(newurl).into(xinyoudetailcircle);

        xinyoudetaillistview=(ListView)findViewById(R.id.spice_xinyou_detail_listview);

        xinyoudetailback=(ImageButton)findViewById(R.id.spice_xinyou_detail_btn);
        xinyoudetailback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onsuccess(String result, int requestcode) {
        Gson gson=new Gson();
        SpiceXinYouDetailProductInfo spiceXinYouDetailProductInfo = gson.fromJson(result, SpiceXinYouDetailProductInfo.class);
        for (int i=0;i<spiceXinYouDetailProductInfo.getList().size();i++){
            listBeans.add(spiceXinYouDetailProductInfo.getList().get(i));
        }
        adapter=new SpiceXinyouDeatilAdapter();
        xinyoudetaillistview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class SpiceXinyouDeatilAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return listBeans.size();
        }

        @Override
        public Object getItem(int i) {
            return listBeans.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            SpiceXinyouViewHolder spiceXinyouViewHolder=null;
            if (view==null){
                view= LayoutInflater.from(ActivityXinYouDetail.this).inflate(R.layout.spice_xinyou_detail_inflater,null);
                spiceXinyouViewHolder=new SpiceXinyouViewHolder();
                spiceXinyouViewHolder.imageView=(ImageView)view.findViewById(R.id.spice_xinyou_detail_inflater_image);
                spiceXinyouViewHolder.title=(TextView)view.findViewById(R.id.spice_xinyou_detail_inflater_title);
                spiceXinyouViewHolder.juese=(TextView)view.findViewById(R.id.spice_xinyou_detail_inflater_content1);
                spiceXinyouViewHolder.size=(TextView)view.findViewById(R.id.spice_xinyou_detail_inflater_content2);
                spiceXinyouViewHolder.subtitle=(TextView)view.findViewById(R.id.spice_xinyou_detail_inflater_content3);
                spiceXinyouViewHolder.btn=(Button)view.findViewById(R.id.spice_xinyou_detail_inflater_btn);
                view.setTag(spiceXinyouViewHolder);
            }else{
                spiceXinyouViewHolder=(SpiceXinyouViewHolder)view.getTag();
            }
            Picasso.with(ActivityXinYouDetail.this).load("http://www.1688wan.com"+listBeans.get(i).getIconurl()).into(spiceXinyouViewHolder.imageView);
            spiceXinyouViewHolder.title.setText(listBeans.get(i).getAppname());
            spiceXinyouViewHolder.juese.setText("类型: "+listBeans.get(i).getTypename());
            spiceXinyouViewHolder.size.setText("大小: "+listBeans.get(i).getAppsize());
            spiceXinyouViewHolder.subtitle.setText(listBeans.get(i).getDescs());
           final Intent intent = new Intent(ActivityXinYouDetail.this, DownloadActivity.class);
            spiceXinyouViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("id", listBeans.get(i).getAppid());
                    startActivity(intent);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("id", listBeans.get(i).getAppid());
                    startActivity(intent);
                }
            });
            return view;
        }
    }
    class SpiceXinyouViewHolder{
        private ImageView imageView;
        private TextView title;
        private TextView juese;
        private TextView size;
        private TextView subtitle;
        private Button btn;
    }
}
