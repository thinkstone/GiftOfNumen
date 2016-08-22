package com.example.shiwei.giftofnumen.kaifu;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shiwei.giftofnumen.BaseFragment;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.kaifu.fragment.Fragment_KaiCe;
import com.example.shiwei.giftofnumen.kaifu.fragment.Fragment_KaiFu;
import com.example.shiwei.giftofnumen.kaifu.fragment.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KaiFuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KaiFuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KaiFuFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TabLayout tablelayout;
    private ViewPager viewpager;
    private List<String> title=new ArrayList<>();
    private List<Fragment> fragments=new ArrayList<>();

    public KaiFuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KaiFuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KaiFuFragment newInstance(String param1, String param2) {
        KaiFuFragment fragment = new KaiFuFragment();
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
        View view=inflater.inflate(R.layout.fragment_kai_fu, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tablelayout = (TabLayout)view.findViewById(R.id.kaifu_tablelayout);
        tablelayout.setSelectedTabIndicatorColor(Color.RED);
        tablelayout.setTabTextColors(Color.GRAY, Color.RED);
        tablelayout.setSelectedTabIndicatorHeight(2);
        viewpager = (ViewPager) view.findViewById(R.id.kaifu_viewpager);
        title.add("开服");
        title.add("开测");
        fragments.add(new Fragment_KaiFu());
        fragments.add(new Fragment_KaiCe());
        viewpager.setAdapter(new ViewPagerAdapter(getFragmentManager(), title, fragments));
        tablelayout.setupWithViewPager(viewpager);

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
}
