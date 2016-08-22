package com.example.shiwei.giftofnumen.spice;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiwei.giftofnumen.BaseFragment;
import com.example.shiwei.giftofnumen.R;
import com.example.shiwei.giftofnumen.spice.fragments.Fragment_Spice_Third;
import com.example.shiwei.giftofnumen.spice.fragments.Fragment_Spice_xinyou;
import com.example.shiwei.giftofnumen.spice.fragments.SpiceViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpiceFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TabLayout spiceTableLayout;
    private ViewPager spiceViewpager;
    private List<String> titlelist=new ArrayList<>();
    private List<Fragment> fragmentslist=new ArrayList<>();

    public SpiceFragment() {
        // Required empty public constructor
    }


    public static SpiceFragment newInstance(String param1, String param2) {
        SpiceFragment fragment = new SpiceFragment();
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
        View view=inflater.inflate(R.layout.fragment_spice, container, false);
        spiceTableLayout=(TabLayout)view.findViewById(R.id.spice_tablelayout);
        spiceTableLayout.setSelectedTabIndicatorColor(Color.RED);
        spiceTableLayout.setTabTextColors(Color.GRAY, Color.RED);
        spiceTableLayout.setSelectedTabIndicatorHeight(2);
        spiceViewpager=(ViewPager)view.findViewById(R.id.spice_viewpager);
        titlelist.add("暴打星期三");
        fragmentslist.add(new Fragment_Spice_Third());
        titlelist.add("新游周刊");
        fragmentslist.add(new Fragment_Spice_xinyou());
        SpiceViewPagerAdapter spiceViewPagerAdapter=new SpiceViewPagerAdapter(getFragmentManager(),titlelist,fragmentslist);
        spiceViewpager.setAdapter(spiceViewPagerAdapter);
        spiceTableLayout.setupWithViewPager(spiceViewpager);
        return view;
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
