package com.example.shiwei.giftofnumen.kaifu.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shiwei.giftofnumen.BaseFragment;
import com.example.shiwei.giftofnumen.DownloadActivity;
import com.example.shiwei.giftofnumen.HttpTool.HttpCallBack;
import com.example.shiwei.giftofnumen.HttpTool.HttpUtils;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.kaifu.bean.KaiFuProductInfo;
import com.example.shiwei.giftofnumen.kaifu.httpadress.HttpAdressKaiFu;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_KaiFu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_KaiFu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_KaiFu extends BaseFragment implements HttpCallBack {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Map<String,List<KaiFuProductInfo.InfoBean>> map = new HashMap<>();
    private List<KaiFuProductInfo.InfoBean> kaifuList;
    private List<String> titlelist = new ArrayList<>();
    private Set<String> titleset = new HashSet<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ExpandableListView expandablelistview;
    private ExpandableListViewAdapter adapter;

    public Fragment_KaiFu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_KaiFu.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_KaiFu newInstance(String param1, String param2) {
        Fragment_KaiFu fragment = new Fragment_KaiFu();
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
        View view = inflater.inflate(R.layout.fragment_fragment__kai_fu, container, false);
        //初始化视图；
        initView(view);
        //请求网络
        getHttp();
        return view;
    }

    private void getHttp() {
        HttpUtils.getHttp(HttpAdressKaiFu.expandablelistviewKaifu, this, 0);
    }

    private void initView(View view) {
        expandablelistview = (ExpandableListView) view.findViewById(R.id.kaifu_expandablelistview);
        expandablelistview.setDivider(null);
        expandablelistview.setGroupIndicator(null);
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
        Gson gson = new Gson();
        KaiFuProductInfo kaiFuProductInfo = gson.fromJson(result, KaiFuProductInfo.class);
        for (int j = 0; j < kaiFuProductInfo.getInfo().size(); j++) {
            titleset.add(kaiFuProductInfo.getInfo().get(j).getAddtime());
        }
        titlelist.addAll(titleset);
        for (int m=0;m<titlelist.size();m++){
            Log.i("SSWW", "onsuccess: "+titlelist.get(m));
        }
        for (int m = 0; m < titlelist.size(); m++) {
            kaifuList = new ArrayList<>();
            for (int i = 0; i <kaiFuProductInfo.getInfo().size(); i++) {
                KaiFuProductInfo.InfoBean infoBean = kaiFuProductInfo.getInfo().get(i);
                if (titlelist.get(m).equals(infoBean.getAddtime())) {
                    //添加组的子数据
                    kaifuList.add(infoBean);
                }
            }
            map.put(titlelist.get(m), kaifuList);
        }
        Set<String> strings = map.keySet();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            List<KaiFuProductInfo.InfoBean> infoBeans = map.get(next);
            Log.i("SSWW", "onsuccess: "+infoBeans.size());
        }
        //遍历childview里面的数据源
        adapter = new ExpandableListViewAdapter();
        expandablelistview.setAdapter(adapter);
        //设置explandablelistview常展开
        for(int i=0;i<adapter.getGroupCount();i++){
            expandablelistview.expandGroup(i);
        }
        //设置expandablelistview不可点击
        expandablelistview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        adapter.notifyDataSetChanged();
    }

    class ExpandableListViewAdapter extends BaseExpandableListAdapter {
        private List<String> list;

        public ExpandableListViewAdapter() {
            Set<String> strings = map.keySet();
            Iterator<String> iterator = strings.iterator();
            list = new ArrayList<>();
            while (iterator.hasNext()) {
                String next = iterator.next();
                list.add(next);
            }
        }

        @Override
        public int getGroupCount() {
            return list.size();
        }
        //显示子控件出现的条目
        @Override
        public int getChildrenCount(int i) {
            return map.get(list.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return i;
        }

        @Override
        public Object getChild(int i, int i1) {
            return i1;
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            GroupViewHolder groupViewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.kaifu_expandablelistview_group, null);
                groupViewHolder = new GroupViewHolder();
                groupViewHolder.titlekaifu = (TextView) view.findViewById(R.id.kaifu_group_title);
                view.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) view.getTag();
            }
            //将标题经行排序
            Collections.sort(list);
            groupViewHolder.titlekaifu.setText(list.get(i));
            return view;
        }

        @Override
        public View getChildView(int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
            ChildViewHolder childViewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.kaifu_expandablelistview_child, null);
                childViewHolder = new ChildViewHolder();
                childViewHolder.button = (Button) view.findViewById(R.id.kaifulist_btn);
                childViewHolder.title = (TextView) view.findViewById(R.id.kaifulist_title);
                childViewHolder.subtitle = (TextView) view.findViewById(R.id.kaifulist_subtitle);
                childViewHolder.content = (TextView) view.findViewById(R.id.kaifulist_content);
                childViewHolder.content1 = (TextView) view.findViewById(R.id.kaifulist_content1);
                childViewHolder.imageView = (ImageView) view.findViewById(R.id.kaifulist_iamge);
                view.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) view.getTag();
            }
            Set<String> strings = map.keySet();
            Iterator<String> iterator = strings.iterator();
            List<String> list = new ArrayList<>();
            while (iterator.hasNext()) {
                String next = iterator.next();
                list.add(next);
            }
            Collections.sort(list);
            final List<KaiFuProductInfo.InfoBean> infoBeans = map.get(list.get(i));
            childViewHolder.title.setText(infoBeans.get(i1).getGname());
            Picasso.with(getActivity()).load("http://www.1688wan.com" + infoBeans.get(i1).getIconurl()).into(childViewHolder.imageView);
            childViewHolder.subtitle.setText(infoBeans.get(i1).getLinkurl());
            childViewHolder.content.setText("运营商：" + infoBeans.get(i1).getOperators());
            childViewHolder.content1.setText(infoBeans.get(i1).getArea());

            final Intent intend = new Intent(getActivity(), DownloadActivity.class);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intend.putExtra("id", infoBeans.get(i1).getGid());
                    startActivity(intend);
                }
            });
            childViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intend.putExtra("id", infoBeans.get(i1).getGid());
                    startActivity(intend);
                }
            });
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
    }

    class GroupViewHolder {
        private TextView titlekaifu;
    }
    class ChildViewHolder {
        private ImageView imageView;
        private TextView title;
        private TextView subtitle;
        private TextView content;
        private TextView content1;
        private Button button;
    }
}
