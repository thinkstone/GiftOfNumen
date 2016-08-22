package com.example.shiwei.giftofnumen.gift;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.example.shiwei.giftofnumen.BaseFragment;
import com.example.shiwei.giftofnumen.CircleActivity;
import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.gift.activity.ActivityDetail;
import com.example.shiwei.giftofnumen.gift.activity.SerachActivity;
import com.example.shiwei.giftofnumen.gift.address.GiftHttpAdress;
import com.example.shiwei.giftofnumen.gift.bean.ProductInfoGift;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GiftFragment extends BaseFragment implements HttpCallBack{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String key="pageno";
    private OnFragmentInteractionListener mListener;
    private PullToRefreshListView putoListView;
    private HashMap<String, Object> map;
    private List<ProductInfoGift.ListBean> productInfoGiftList=new ArrayList<>();
    private List<String> productInfoGiftListAd=new ArrayList<>();
    private PutllreFreshAdapter putorefreshadapter;
    private int page=1;
    private Gson gson=new Gson();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                putoListView.onRefreshComplete();
                putorefreshadapter.notifyDataSetChanged();
            }
        }
    };
    private ConvenientBanner convientbanner;
    private Button serach;
    private DrawerLayout slidelinear;
    private ImageButton imagebutton;
    private LinearLayout linear;
    private CircleImageView slide_circle;

    public GiftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GiftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GiftFragment newInstance(String param1, String param2) {
        GiftFragment fragment = new GiftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_gift, container, false);
        //初始化视图
        initView(view);
        //请求网络
        getHttp();
        return view;
    }

    private void getHttp() {
        map=new HashMap<>();
        map.put(key, page);
        HttpUtils.postHttp(GiftHttpAdress.gift, this, 0, map);
    }

    private void initView(View view) {
        //listview控件
        putoListView=(PullToRefreshListView)view.findViewById(R.id.gift_putorefreshlistview);
        putoListView=(PullToRefreshListView)view.findViewById(R.id.gift_putorefreshlistview);
        putoListView.setMode(PullToRefreshBase.Mode.BOTH);
        imagebutton=(ImageButton)view.findViewById(R.id.gift_imagebutton);
        linear=(LinearLayout)getActivity().findViewById(R.id.mainactivity_slide_linear);
        slidelinear=(DrawerLayout)getActivity().findViewById(R.id.mainactivity_drawlayout);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slidelinear.isDrawerOpen(linear)){
                    slidelinear.closeDrawer(linear);
                }else{
                    slidelinear.openDrawer(linear);
                }
            }
        });
        slide_circle=(CircleImageView)getActivity().findViewById(R.id.mainactivity_slide_circle);
        slide_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),CircleActivity.class);
                startActivity(intent);
            }
        });
        serach=(Button)view.findViewById(R.id.gift_serach);
        serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SerachActivity.class);
                startActivity(intent);
            }
        });

        //设置listview的适配器
        putorefreshadapter=new PutllreFreshAdapter();
        putoListView.setAdapter(putorefreshadapter);

        //为pulltolistview添加头部视图广告牌(在网络请求后面加载)
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.convientbanner, putoListView, false);
        header.setLayoutParams(layoutParams);
        ListView refreshableView = putoListView.getRefreshableView();
        refreshableView.addHeaderView(header);
        convientbanner=(ConvenientBanner)header.findViewById(R.id.gift_convientbanner);

        //刷新listview
        putoListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                page = 1;
                map.put(key, page);
                HttpUtils.postHttp(GiftHttpAdress.gift, new HttpCallBack() {
                    @Override
                    public void onsuccess(String result, int requestcode) {
                        ProductInfoGift productInfoGift = gson.fromJson(result, ProductInfoGift.class);
                        productInfoGiftList.clear();
                        productInfoGiftListAd.clear();
                        if (productInfoGift != null) {
                            for (int i = 0; i < productInfoGift.getList().size(); i++) {
                                productInfoGiftList.add(productInfoGift.getList().get(i));
                            }
                            for (int j=0;j<productInfoGift.getAd().size();j++){
                                productInfoGiftListAd.add("http://www.1688wan.com"+productInfoGift.getAd().get(j).getIconurl());
                            }
                            //加载广告牌要写在适配器之后
                            convientbanner.setPages(new CBViewHolderCreator<BannerViewHolder>() {
                                @Override
                                public BannerViewHolder createHolder() {
                                    return new BannerViewHolder();
                                }
                            },productInfoGiftListAd).setPageIndicator(new int[]{R.drawable.whitedot, R.drawable.reddot}).
                                    setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL).startTurning(3000);
                        }
                    }
                }, 0, map);
                handler.sendEmptyMessageDelayed(1, 2000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                page = page + 2;
                map.put(key, page);
                HttpUtils.postHttp(GiftHttpAdress.gift, new HttpCallBack() {
                    @Override
                    public void onsuccess(String result, int requestcode) {
                        ProductInfoGift productInfoGift = gson.fromJson(result, ProductInfoGift.class);
                        if (productInfoGift != null) {
                            for (int i = 0; i < productInfoGift.getList().size(); i++) {
                                productInfoGiftList.add(productInfoGift.getList().get(i));
                            }
                        }
                    }
                }, 0, map);
                //发送延时消息经行UI更新
                handler.sendEmptyMessageDelayed(1, 2000);
            }
        });

    }
    class BannerViewHolder implements Holder<String> {
        public ImageView mImageView;
        @Override
        public View createView(Context context) {
            mImageView = new ImageView(getActivity());
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return mImageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Picasso.with(getActivity()).load(data).into(mImageView);
        }
    }


    @Override
    public void onsuccess(String result, int requestcode) {
        ProductInfoGift productInfoGift = gson.fromJson(result, ProductInfoGift.class);
        if (productInfoGift!=null){
            for (int i=0;i<productInfoGift.getList().size();i++){
                productInfoGiftList.add(productInfoGift.getList().get(i));
            }
            for(int j=0;j<productInfoGift.getAd().size();j++){
                productInfoGiftListAd.add("http://www.1688wan.com"+productInfoGift.getAd().get(j).getIconurl());
            }
            putorefreshadapter.notifyDataSetChanged();
        }

        //加载广告牌要写在适配器之后
        convientbanner.setPages(new CBViewHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createHolder() {
                return new BannerViewHolder();
            }
        },productInfoGiftListAd).setPageIndicator(new int[]{R.drawable.whitedot, R.drawable.reddot}).
                setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL).startTurning(3000);

        putoListView.setScrollingWhileRefreshingEnabled(true);

    }

    class PutllreFreshAdapter extends BaseAdapter{
        private LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
        private ViewHolder viewholder;
        private Bitmap bitmap;
        private Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        @Override
        public int getCount() {
            return productInfoGiftList.size();
        }

        @Override
        public Object getItem(int i) {
            return productInfoGiftList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            viewholder=null;
            if (view==null){
                view=layoutInflater.inflate(R.layout.gife_inflater_listview,null);
                viewholder=new ViewHolder();
                viewholder.button= (Button) view.findViewById(R.id.giftlist_btn);
                viewholder.title= (TextView) view.findViewById(R.id.giftlist_title);
                viewholder.subTitle= (TextView) view.findViewById(R.id.giftlist_subtitle);
                viewholder.content= (TextView) view.findViewById(R.id.giftlist_content);
                viewholder.content1= (TextView) view.findViewById(R.id.giftlist_content1);
                viewholder.imageView= (ImageView) view.findViewById(R.id.giftlist_iamge);
                view.setTag(viewholder);
            }else {
                viewholder=(ViewHolder)view.getTag();
            }
            //设置点击每个视图的点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ActivityDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", productInfoGiftList.get(i).getId());
                    bundle.putString("title", productInfoGiftList.get(i).getGname());
                    bundle.putString("subtitle", productInfoGiftList.get(i).getGiftname());
                    bundle.putInt("number", productInfoGiftList.get(i).getNumber());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            //在子线程里面获取图片
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        //请求图片网络框架
//                        URL url = new URL("http://www.1688wan.com"+productInfoGiftList.get(i).getIconurl());
//                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                        connection.setDoOutput(true);
//                        connection.setRequestMethod("POST");
//                        connection.connect();
//                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                            InputStream inputStream = connection.getInputStream();
//                            bitmap = BitmapFactory.decodeStream(inputStream);
//                        }
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    handler.sendEmptyMessage(0);
//                }
//            }).start();
//            viewholder.imageView.setImageBitmap(bitmap);
            Picasso.with(getActivity()).load("http://www.1688wan.com"+productInfoGiftList.get(i).getIconurl()).into(viewholder.imageView);
            viewholder.title.setText(productInfoGiftList.get(i).getGname());
            viewholder.subTitle.setText(productInfoGiftList.get(i).getGiftname());
            viewholder.content.setText("剩余: "+productInfoGiftList.get(i).getNumber());
            viewholder.content1.setText("时间: "+productInfoGiftList.get(i).getAddtime());
            viewholder.content1.setText("时间: " + productInfoGiftList.get(i).getAddtime());
            if (productInfoGiftList.get(i).getNumber()==0&&!productInfoGiftList.isEmpty()){
                viewholder.button.setBackgroundResource(R.drawable.taojin);
                viewholder.button.setText("淘  号");
                viewholder.button.setTextColor(Color.WHITE);
            }else {
                viewholder.button.setBackgroundResource(R.drawable.lingqurigthnow);
                viewholder.button.setText("立即领取");
                viewholder.button.setTextColor(Color.WHITE);
            }
            viewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(), ActivityDetail.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("id", productInfoGiftList.get(i).getId());
                    bundle.putString("title", productInfoGiftList.get(i).getGname());
                    bundle.putString("subtitle", productInfoGiftList.get(i).getGiftname());
                    bundle.putInt("number", productInfoGiftList.get(i).getNumber());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return view;
        }
        class ViewHolder{
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