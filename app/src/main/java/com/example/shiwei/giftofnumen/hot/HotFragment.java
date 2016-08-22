package com.example.shiwei.giftofnumen.hot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shiwei.giftofnumen.BaseFragment;
import com.example.shiwei.giftofnumen.DownloadActivity;
import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.customerview.CustomGridView;
import com.example.shiwei.giftofnumen.customerview.CustomerListview;
import com.example.shiwei.giftofnumen.hot.bean.HotProductInfo;
import com.example.shiwei.giftofnumen.hot.httpadress.HotHttpAdress;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotFragment extends BaseFragment implements HttpCallBack {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView hotlistview;
    private CustomGridView hotgridview;
    private List<HotProductInfo.InfoBean.Push1Bean> listviewlist=new ArrayList<>();
    private List<HotProductInfo.InfoBean.Push2Bean> gridviewlist=new ArrayList<>();

    public HotFragment() {
        // Required empty public constructor
    }

    public static HotFragment newInstance(String param1, String param2) {
        HotFragment fragment = new HotFragment();
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
        View view=inflater.inflate(R.layout.fragment_hot, container, false);
        hotlistview=(CustomerListview)view.findViewById(R.id.hot_listview);
        hotgridview=(CustomGridView)view.findViewById(R.id.hot_gridview);
        //请求网络；
        gethttp();
        return view;
    }

    private void gethttp() {
        HttpUtils.getHttp(HotHttpAdress.hotadress,this,0);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onsuccess(String result, int requestcode) {
        Gson gson=new Gson();
        HotProductInfo hotProductInfo = gson.fromJson(result, HotProductInfo.class);
        for (int i=0;i<hotProductInfo.getInfo().getPush1().size();i++){
            listviewlist.add(hotProductInfo.getInfo().getPush1().get(i));
        }
        HotListViewAdapter adapter=new HotListViewAdapter();
        hotlistview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        for (int i=0;i<hotProductInfo.getInfo().getPush2().size();i++){
            gridviewlist.add(hotProductInfo.getInfo().getPush2().get(i));
        }
        HotGridViewAdapter adapter1=new HotGridViewAdapter();
        hotgridview.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
    }
    class HotListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listviewlist.size();
        }

        @Override
        public Object getItem(int i) {
            return listviewlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            HotlistViewHolder hotlistViewHolder=null;
            if (view==null){
                view=LayoutInflater.from(getActivity()).inflate(R.layout.hot_listview_inflater,null);
                hotlistViewHolder=new HotlistViewHolder();
                hotlistViewHolder.imageView=(ImageView)view.findViewById(R.id.hot_listview_inflater_imageicon);
                hotlistViewHolder.textView1=(TextView)view.findViewById(R.id.hot_listview_inflater_text1);
                hotlistViewHolder.textView2=(TextView)view.findViewById(R.id.hot_listview_inflater_text2);
                hotlistViewHolder.textView3=(TextView)view.findViewById(R.id.hot_listview_inflater_text3);
                hotlistViewHolder.textView4=(TextView)view.findViewById(R.id.hot_listview_inflater_text4);
                hotlistViewHolder.textView5=(TextView)view.findViewById(R.id.hot_listview_inflater_text5);
                view.setTag(hotlistViewHolder);
            }else {
                hotlistViewHolder=(HotlistViewHolder)view.getTag();
            }
            Picasso.with(getActivity()).load("http://www.1688wan.com" + listviewlist.get(i).getLogo()).into(hotlistViewHolder.imageView);
            hotlistViewHolder.textView1.setText(listviewlist.get(i).getName());
            hotlistViewHolder.textView2.setText(listviewlist.get(i).getTypename());
            if (listviewlist.get(i).getSize()==null){
                hotlistViewHolder.textView3.setText("大小：不大");
            }else{
                hotlistViewHolder.textView3.setText("大小："+listviewlist.get(i).getSize());
            }
            if (listviewlist.get(i).getClicks()!=0&&!listviewlist.isEmpty()){
                hotlistViewHolder.textView4.setText(""+listviewlist.get(i).getClicks());
                hotlistViewHolder.textView5.setText("人在玩");
            }
            final Intent intent=new Intent(getActivity(), DownloadActivity.class);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("id",listviewlist.get(i).getAppid());
                    startActivity(intent);
                }
            });
            return view;
        }
        class HotlistViewHolder{
            private ImageView imageView;
            private TextView textView1;
            private TextView textView2;
            private TextView textView3;
            private TextView textView4;
            private TextView textView5;
        }
    }


    class HotGridViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return gridviewlist.size();
        }

        @Override
        public Object getItem(int i) {
            return gridviewlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            HotGridlistViewHolder hotGridlistViewHolder=null;
            if (view==null){
                view=LayoutInflater.from(getActivity()).inflate(R.layout.hot_gridview_inflater,null);
                hotGridlistViewHolder=new HotGridlistViewHolder();
                hotGridlistViewHolder.imageView=(ImageView)view.findViewById(R.id.hot_gridview_inflater_imageview);
                hotGridlistViewHolder.textView=(TextView)view.findViewById(R.id.hot_gridview_inflater_text);
                view.setTag(hotGridlistViewHolder);
            }else{
                hotGridlistViewHolder=(HotGridlistViewHolder)view.getTag();
            }
            Picasso.with(getActivity()).load("http://www.1688wan.com"+gridviewlist.get(i).getLogo()).into(hotGridlistViewHolder.imageView);
            hotGridlistViewHolder.textView.setText(gridviewlist.get(i).getName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DownloadActivity.class);
                    intent.putExtra("id", gridviewlist.get(i).getAppid());
                    startActivity(intent);
                }
            });
            return view;
        }
        class  HotGridlistViewHolder{
            private ImageView imageView;
            private TextView textView;
        }
    }
}
