package com.example.shiwei.giftofnumen.gift.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.example.shiwei.giftofnumen.MyActivity;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.customerview.CustomerListview;
import com.example.shiwei.giftofnumen.gift.address.GiftHttpAdress;
import com.example.shiwei.giftofnumen.gift.bean.ProductInfoSearch;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shiwei on 2016/8/17.
 */
public class SerachActivity extends MyActivity implements HttpCallBack {
    private ImageButton imagebtn;
    private EditText edittext;
    private Button searchbtn;
    private CustomerListview listview;
    private int page;
    private Gson gson;
    private List<ProductInfoSearch.ListBean> searchlistbean = new ArrayList<>();
    private String getString;
    private PutllreFreshAdapter adapter;
    private HashMap<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_serach_activity);
        //初始化视图
        initview();
        //请求网络
        gethttp();
    }

    private void gethttp() {
        map = new HashMap<>();
        page = 1;
        int respondcode = 0;
        map.put("key",page);
        HttpUtils.postHttp(GiftHttpAdress.searchadress, this, respondcode, map);
    }

    private void initview() {
        gson = new Gson();
        imagebtn = (ImageButton) findViewById(R.id.gift_serach_back);
        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SerachActivity.this.finish();
            }
        });

        edittext = (EditText) findViewById(R.id.gift_serach_edittext);
        getString = edittext.getEditableText().toString();

        //监听文字改变
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //设置文字改变的监听
                getString = edittext.getEditableText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        searchbtn = (Button) findViewById(R.id.gift_serach_serachbtn);

        listview = (CustomerListview) findViewById(R.id.gift_serach_listview);
    }

    @Override
    public void onsuccess(String result, int requestcode) {
        ProductInfoSearch productInfoSearch = gson.fromJson(result, ProductInfoSearch.class);
        for (int i = 0; i < productInfoSearch.getList().size(); i++) {
            searchlistbean.add(productInfoSearch.getList().get(i));
        }
        adapter = new PutllreFreshAdapter(searchlistbean);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getString.isEmpty()) {
                    Toast.makeText(SerachActivity.this, "请输入关键字", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progressDialog=new ProgressDialog(SerachActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("正在加载中");
                    progressDialog.show();
                    map.clear();
                    map.put("key", getString);
                    HttpUtils.postHttp(GiftHttpAdress.searchadress, new HttpCallBack() {
                        @Override
                        public void onsuccess(String result, int requestcode) {
                            ProductInfoSearch productInfoSearch1 = gson.fromJson(result, ProductInfoSearch.class);
                            if (result != null&&productInfoSearch1.getList().size()!=0) {
                                progressDialog.dismiss();
                            }
                            for (int i = 0; i < productInfoSearch1.getList().size(); i++) {
                                searchlistbean.clear();
                                searchlistbean.add(productInfoSearch1.getList().get(i));
                            }
                        }
                    }, 0, map);
                    if (searchlistbean.size()!= 0) {
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SerachActivity.this, "没有找到相关数据", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    class PutllreFreshAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater = LayoutInflater.from(SerachActivity.this);
        private ViewHolder viewholder;
        private List<ProductInfoSearch.ListBean> searchlistbean;

        public PutllreFreshAdapter(List<ProductInfoSearch.ListBean> searchlistbean) {
            this.searchlistbean = searchlistbean;
        }

        @Override
        public int getCount() {
            return searchlistbean.size();
        }

        @Override
        public Object getItem(int i) {
            return searchlistbean.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            viewholder = null;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.gife_inflater_listview, null);
                viewholder = new ViewHolder();
                viewholder.button = (Button) view.findViewById(R.id.giftlist_btn);
                viewholder.title = (TextView) view.findViewById(R.id.giftlist_title);
                viewholder.subTitle = (TextView) view.findViewById(R.id.giftlist_subtitle);
                viewholder.content = (TextView) view.findViewById(R.id.giftlist_content);
                viewholder.content1 = (TextView) view.findViewById(R.id.giftlist_content1);
                viewholder.imageView = (ImageView) view.findViewById(R.id.giftlist_iamge);
                view.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) view.getTag();
            }
            //设置点击每个视图的点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SerachActivity.this, ActivityDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", searchlistbean.get(i).getId());
                    bundle.putString("title", searchlistbean.get(i).getGname());
                    bundle.putString("subtitle", searchlistbean.get(i).getGiftname());
                    bundle.putInt("number", searchlistbean.get(i).getNumber());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            Picasso.with(SerachActivity.this).load("http://www.1688wan.com" + searchlistbean.get(i).getIconurl()).into(viewholder.imageView);
            viewholder.title.setText(searchlistbean.get(i).getGname());
            viewholder.subTitle.setText(searchlistbean.get(i).getGiftname());
            viewholder.content.setText("剩余: " + searchlistbean.get(i).getNumber());
            viewholder.content1.setText("时间: " + searchlistbean.get(i).getAddtime());
            viewholder.content1.setText("时间: " + searchlistbean.get(i).getAddtime());
            if (searchlistbean.get(i).getNumber() == 0 && !searchlistbean.isEmpty()) {
                viewholder.button.setBackgroundResource(R.drawable.taojin);
                viewholder.button.setText("淘  号");
                viewholder.button.setTextColor(Color.WHITE);
            } else {
                viewholder.button.setBackgroundResource(R.drawable.lingqurigthnow);
                viewholder.button.setText("立即领取");
                viewholder.button.setTextColor(Color.WHITE);
            }
            viewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SerachActivity.this, ActivityDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", searchlistbean.get(i).getId());
                    bundle.putString("title", searchlistbean.get(i).getGname());
                    bundle.putString("subtitle", searchlistbean.get(i).getGiftname());
                    bundle.putInt("number", searchlistbean.get(i).getNumber());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return view;
        }

        class ViewHolder {
            public TextView title;
            public TextView subTitle;
            public TextView content;
            public TextView content1;
            public ImageView imageView;
            public Button button;

            public ViewHolder() {
            }
        }
    }
}
