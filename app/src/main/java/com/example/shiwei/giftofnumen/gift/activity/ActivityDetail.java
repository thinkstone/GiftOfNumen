package com.example.shiwei.giftofnumen.gift.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.example.shiwei.giftofnumen.MyActivity;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.gift.address.GiftHttpAdress;
import com.example.shiwei.giftofnumen.gift.bean.ProductIndoDetail;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiwei on 2016/8/14.
 */
public class ActivityDetail extends MyActivity implements HttpCallBack {
    private ImageButton back;
    private ImageButton share;
    private TextView title;
    private TextView number;
    private TextView date;
    private int number1;
    private String id;
    private String subtitle;
    private String title1;
    private Button lijilingqu;
    public static final String key="id";
    private Map<String,Object> map=new HashMap<>();
    private Gson gson=new Gson();
    private CircleImageView cicleoimage;
    private TextView libaoshuoming;
    private TextView duihuanfangshi;
    private ImageView icon_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_detail);
        Bundle extras = getIntent().getExtras();
        number1 = extras.getInt("number");
        id=extras.getString("id");
        subtitle = extras.getString("subtitle");
        title1=extras.getString("title");
        initView();
        //获取网络请求
        gethttpResult();

    }

    private void gethttpResult() {
        map.put(key,id);
        HttpUtils.postHttp(GiftHttpAdress.giftdetail,this,0,map);
    }

    private void initView() {
        back = (ImageButton)findViewById(R.id.gift_detail_back);
        share=(ImageButton)findViewById(R.id.gift_detail_share);
        cicleoimage=(CircleImageView)findViewById(R.id.gift_detail_imageview);
        lijilingqu=(Button)findViewById(R.id.gift_detail_lijilingqu);
        libaoshuoming=(TextView)findViewById(R.id.gift_detail_libaoshuoming);
        duihuanfangshi=(TextView)findViewById(R.id.gift_detail_duihuangfangshi);
        if (number1==0){
            lijilingqu.setText("马上淘号");
        }else {
            lijilingqu.setText("立即领取");
        }
        title=(TextView)findViewById(R.id.gift_detail_title);
        title.setText(title1+"-"+subtitle);

        number=(TextView)findViewById(R.id.gift_detail_number);
        number.setText("礼包剩余:"+number1);
        date=(TextView)findViewById(R.id.gift_detail_date);
        icon_image=(ImageView)findViewById(R.id.gift_detail_imageview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityDetail.this.finish();
            }
        });
    }

    @Override
    public void onsuccess(String result, int requestcode) {
        ProductIndoDetail productIndoDetail = gson.fromJson(result, ProductIndoDetail.class);
        Picasso.with(ActivityDetail.this).load("http://www.1688wan.com" + productIndoDetail.getInfo().getIconurl()).into(cicleoimage);
        date.setText("有效期:" + productIndoDetail.getInfo().getOvertime());
        libaoshuoming.setText(productIndoDetail.getInfo().getExplains());
        duihuanfangshi.setText(productIndoDetail.getInfo().getDescs());
    }
}
