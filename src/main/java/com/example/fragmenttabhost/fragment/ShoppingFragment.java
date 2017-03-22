package com.example.fragmenttabhost.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fragmenttabhost.R;

/**
 * Created by Richie on 2017/3/14.
 */

public class ShoppingFragment extends BaseFragment{
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.shoppinfragment_layout,container,false);

        return view;
    }

    @Override
    public void init() {

    }
}
