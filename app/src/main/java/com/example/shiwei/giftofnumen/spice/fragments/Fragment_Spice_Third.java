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
import android.widget.TextView;

import com.example.shiwei.giftofnumen.BaseFragment;
import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.spice.activity.SpiceActivityDetail;
import com.example.shiwei.giftofnumen.spice.fragments.bean.SpiceThirdProductInfo;
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
 * {@link Fragment_Spice_Third.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Spice_Third#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Spice_Third extends BaseFragment implements HttpCallBack{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<SpiceThirdProductInfo.ListBean> spiceThirdproList=new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private PullToRefreshListView spicePutorfresh;
    private SpiceThirdListView adapter;

    public Fragment_Spice_Third() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Spice_Third.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Spice_Third newInstance(String param1, String param2) {
        Fragment_Spice_Third fragment = new Fragment_Spice_Third();
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
        // Inflate the layout for this fragmen
        View view=inflater.inflate(R.layout.fragment__spice__third, container, false);
        spicePutorfresh=(PullToRefreshListView)view.findViewById(R.id.spice_third_putorefresh);
        spicePutorfresh.setMode(PullToRefreshBase.Mode.BOTH);
        //请求网络
        getHttp();
        return view;
    }

    private void getHttp() {
        HttpUtils.getHttp(SpiceAdress.spice_third,this,0);
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
        SpiceThirdProductInfo spiceThirdProductInfo = gson.fromJson(result, SpiceThirdProductInfo.class);
        for (int i=0;i<spiceThirdProductInfo.getList().size();i++){
            spiceThirdproList.add(spiceThirdProductInfo.getList().get(i));
        }
//        Log.i("SSWW", "onsuccess: "+spiceThirdproList.size());
        //设置适配器并将数据刷新
        adapter=new SpiceThirdListView();
        spicePutorfresh.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class SpiceThirdListView extends BaseAdapter{
        @Override
        public int getCount() {
            return spiceThirdproList.size();
        }

        @Override
        public Object getItem(int i) {
            return spiceThirdproList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            SpiceThirdViewHolder spiceThirdViewHolder=null;
            if (view==null){
                view=LayoutInflater.from(getActivity()).inflate(R.layout.spice_third_inflater,null);
                spiceThirdViewHolder=new SpiceThirdViewHolder();
                spiceThirdViewHolder.imageButton=(ImageButton)view.findViewById(R.id.spice_third_inflater_iamgebtn);
                spiceThirdViewHolder.title=(TextView)view.findViewById(R.id.spice_third_inflater_title);
                spiceThirdViewHolder.subtitle=(TextView)view.findViewById(R.id.spice_third_inflater_subtitle);
                view.setTag(spiceThirdViewHolder);
            }else{
                spiceThirdViewHolder=(SpiceThirdViewHolder)view.getTag();
            }
            if (!spiceThirdproList.isEmpty()&&spiceThirdproList.get(i)!=null){
                Picasso.with(getActivity()).load("http://www.1688wan.com"+spiceThirdproList.get(i).getIconurl()).into(spiceThirdViewHolder.imageButton);
            }
            spiceThirdViewHolder.title.setText(spiceThirdproList.get(i).getName());
            spiceThirdViewHolder.subtitle.setText(spiceThirdproList.get(i).getAddtime());
            spiceThirdViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(), SpiceActivityDetail.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("id",spiceThirdproList.get(i).getSid());
                    bundle.putString("title",spiceThirdproList.get(i).getName());
                    bundle.putString("iconurl",spiceThirdproList.get(i).getIconurl());
                    bundle.putString("content",spiceThirdproList.get(i).getDescs());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return view;
        }

        class SpiceThirdViewHolder{
            private ImageButton imageButton;
            private TextView title;
            private TextView subtitle;
        }
    }
}
