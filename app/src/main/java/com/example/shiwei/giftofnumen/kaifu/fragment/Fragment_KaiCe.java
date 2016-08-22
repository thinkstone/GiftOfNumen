package com.example.shiwei.giftofnumen.kaifu.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiwei.giftofnumen.BaseFragment;
import com.example.shiwei.giftofnumen.DownloadActivity;
import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.kaifu.bean.KaiFuKaiCeProductInfo;
import com.example.shiwei.giftofnumen.kaifu.httpadress.HttpAdressKaiFu;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_KaiCe.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_KaiCe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_KaiCe extends BaseFragment implements HttpCallBack {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private PullToRefreshListView putolistview;
    private List<KaiFuKaiCeProductInfo.InfoBean> productInfoList=new ArrayList<>();
    private KaiFuKaiCeAdapter adapter;

    public Fragment_KaiCe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_KaiCe.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_KaiCe newInstance(String param1, String param2) {
        Fragment_KaiCe fragment = new Fragment_KaiCe();
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
        View view=inflater.inflate(R.layout.fragment_fragment__kai_ce, container, false);
        putolistview = (PullToRefreshListView)view.findViewById(R.id.kaifu_kaice_putolistview);
        putolistview.setMode(PullToRefreshBase.Mode.BOTH);

        //请求网络
        getHttp();
        return view;
    }

    private void getHttp() {
        HttpUtils.getHttp(HttpAdressKaiFu.listviewKaice,this,0);
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
        KaiFuKaiCeProductInfo kaiFuKaiCeProductInfo = gson.fromJson(result, KaiFuKaiCeProductInfo.class);
        for (int i=0;i<kaiFuKaiCeProductInfo.getInfo().size();i++) {
            productInfoList.add(kaiFuKaiCeProductInfo.getInfo().get(i));
        }
        adapter=new KaiFuKaiCeAdapter();
        putolistview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class KaiFuKaiCeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return productInfoList.size();
        }

        @Override
        public Object getItem(int i) {
            return productInfoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder=new ViewHolder();
            if (view==null){
                view= LayoutInflater.from(getActivity()).inflate(R.layout.kaifu_kaice_listview,null);
                viewHolder.button11=(Button)view.findViewById(R.id.kaifu_kaice_btn);
                viewHolder.content1=(TextView)view.findViewById(R.id.kaifu_kaice_content1);
                viewHolder.subTitle=(TextView)view.findViewById(R.id.kaifu_kaice_subtitle);
                viewHolder.title11=(TextView)view.findViewById(R.id.kaifu_kaice_title);
                viewHolder.imageView=(ImageView)view.findViewById(R.id.kaifu_kaice_list_iamge);
                view.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder)view.getTag();
            }
            Picasso.with(getActivity()).load("http://www.1688wan.com"+productInfoList.get(i).getIconurl()).into(viewHolder.imageView);
            viewHolder.title11.setText(productInfoList.get(i).getGname());
            viewHolder.subTitle.setText("运营商: "+productInfoList.get(i).getOperators());
            viewHolder.content1.setText(productInfoList.get(i).getAddtime());
            final Intent intent=new Intent(getActivity(), DownloadActivity.class);
            viewHolder.button11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("id",productInfoList.get(i).getGid());
                    startActivity(intent);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("id",productInfoList.get(i).getGid());
                    startActivity(intent);
                }
            });
            return view;
        }
    }
    class ViewHolder{
        public TextView title11;
        public TextView subTitle;
        public TextView content1;
        public ImageView imageView;
        public Button button11;
        public ViewHolder() {
        }
    }
}
