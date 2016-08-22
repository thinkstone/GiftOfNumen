package com.example.shiwei.giftofnumen.spice.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiwei.giftofnumen.BaseFragment;
import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.spice.activity.ActivityXinYouDetail;
import com.example.shiwei.giftofnumen.spice.fragments.bean.SpiceXinyouProductInfo;
import com.example.shiwei.giftofnumen.spice.fragments.httpadress.SpiceAdress;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Spice_xinyou.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Spice_xinyou#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Spice_xinyou extends BaseFragment implements HttpCallBack {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<SpiceXinyouProductInfo.ListBean> listBeanList=new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private PullToRefreshListView spicexinyoulistview;
    private SpiceXinyouAdapter spiceXinyouAdapter;

    public Fragment_Spice_xinyou() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Spice_xinyou.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Spice_xinyou newInstance(String param1, String param2) {
        Fragment_Spice_xinyou fragment = new Fragment_Spice_xinyou();
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
        // Inflate the layout for this fragment]
        View view=inflater.inflate(R.layout.fragment__spice_xinyou, container, false);
        spicexinyoulistview = (PullToRefreshListView)view.findViewById(R.id.spice_xinyou_listview);
//        spicexinyoulistview.setMode(PullToRefreshBase.Mode.BOTH);
        //请求网络
        gethttp();
        return view;
    }

    private void gethttp() {
        HttpUtils.getHttp(SpiceAdress.spice_xinyou,this,0);
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
        SpiceXinyouProductInfo spiceXinyouProductInfo = gson.fromJson(result, SpiceXinyouProductInfo.class);
        for (int i=0;i<spiceXinyouProductInfo.getList().size();i++){
            listBeanList.add(spiceXinyouProductInfo.getList().get(i));
        }
        //设置适配器
//        Log.i("SSWW", "onsuccess: "+listBeanList.get(0).getNewsicon());
        spiceXinyouAdapter=new SpiceXinyouAdapter();
        spicexinyoulistview.setAdapter(spiceXinyouAdapter);
        spiceXinyouAdapter.notifyDataSetChanged();
    }

    class SpiceXinyouAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return listBeanList.size();
        }

        @Override
        public Object getItem(int i) {
            return listBeanList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            SpiceXinyouViewHolder spiceXinyouViewHolder=null;
            if (view==null){
                view=LayoutInflater.from(getActivity()).inflate(R.layout.spice_xinyou_inflater,null);
                spiceXinyouViewHolder=new SpiceXinyouViewHolder();
                spiceXinyouViewHolder.imageButton=(ImageButton)view.findViewById(R.id.spice_xinyou_inflater_iamgebtn);
                spiceXinyouViewHolder.title=(TextView)view.findViewById(R.id.spice_xinyou_inflater_title);
                spiceXinyouViewHolder.imageView=(ImageView)view.findViewById(R.id.spice_xinyou_inflater_circleimage);
                view.setTag(spiceXinyouViewHolder);
            }else{
                spiceXinyouViewHolder=(SpiceXinyouViewHolder)view.getTag();
            }
            Picasso.with(getActivity()).load(listBeanList.get(i).getNewsicon()).into(spiceXinyouViewHolder.imageButton);
            Picasso.with(getActivity()).load("http://www.1688wan.com"+listBeanList.get(i).getIconurl()).into(spiceXinyouViewHolder.imageView);
            spiceXinyouViewHolder.title.setText(listBeanList.get(i).getName());
            spiceXinyouViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ActivityXinYouDetail.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("id",listBeanList.get(i).getId());
                    bundle.putString("title",listBeanList.get(i).getName());
                    bundle.putString("content",listBeanList.get(i).getDescs());
                    bundle.putString("iconurl",listBeanList.get(i).getIconurl());
                    bundle.putString("newsurl",listBeanList.get(i).getNewsicon());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return view;
        }
    }
    class SpiceXinyouViewHolder{
        private ImageButton imageButton;
        private TextView title;
        private ImageView imageView;
    }
}

