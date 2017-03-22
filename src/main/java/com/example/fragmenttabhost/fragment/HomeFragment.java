package com.example.fragmenttabhost.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.fragmenttabhost.Contants;
import com.example.fragmenttabhost.R;
import com.example.fragmenttabhost.WareListActivity;
import com.example.fragmenttabhost.adapter.decoration.CardViewtemDecortion;
import com.example.fragmenttabhost.adapter.HomeListAdapter;
import com.example.fragmenttabhost.bean.Banner;
import com.example.fragmenttabhost.bean.Campaign;
import com.example.fragmenttabhost.bean.HomeCampaign;
import com.example.fragmenttabhost.utlis.BaseCallback;
import com.example.fragmenttabhost.utlis.OkHttpHelper;
import com.example.fragmenttabhost.utlis.SpotsCallBack;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * Created by Richie on 2017/3/14.
 * 主页
 */

public class HomeFragment extends BaseFragment {
    public static final String TAG = "HomeFragment==";
    public String bannerAPI = "http://112.124.22.238:8081/course_api/banner/query?type=1";
    public String listviewAPI = " http://112.124.22.238:8081/course_api/campaign/recommend";

    @ViewInject(R.id.slider)
    private SliderLayout sliderLayout;
    @ViewInject(R.id.recyclerview)
    private RecyclerView recyclerView;

    private List<Banner> mlistbanner;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private Gson gson = new Gson();


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefragment_layout, container, false);

        return view;
    }

    @Override
    public void init() {
        requestImages();
        initRecyclerView();


    }

    //请求数据
    private void requestImages() {
        okHttpHelper.get(bannerAPI, new SpotsCallBack<List<Banner>>(getActivity()) {
            @Override
            public void onSuccess(Response response, List<Banner> listbanner) {
                mlistbanner = listbanner;
                initSlider();

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.d(TAG, "onError: " + e);

            }
        });
    }


    //初始化广告栏头部
    public void initSlider() {
        if (mlistbanner != null) {
            for (Banner banner : mlistbanner) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());   // 设置图片
                textSliderView.description(banner.getName());  //设置文字
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                sliderLayout.addSlider(textSliderView);      //将textSliderView添加到 SliderLayout
            }

        }

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);    //设置广告栏下面的点的位置
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);  //设置翻页时旋转效果
        sliderLayout.setDuration(3000);   //设置翻页的时间间隔
    }



    //初始化listview数据
    public void initRecyclerView() {

        okHttpHelper.get(listviewAPI, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }


            @Override
            public void onError(Response response, int code, Exception e) {

                Log.d(TAG, "onError: "+e);

            }
        });

    }


      //初始化adapter
    private void initData(List<HomeCampaign> Campagams) {

        HomeListAdapter adapter = new HomeListAdapter(Campagams, getActivity());

        adapter.setCampagmsCliclinser(new HomeListAdapter.CampagamsCliclisner() {
            @Override
            public void onClick(View view, Campaign campagams) {
                Intent intent = new Intent(getActivity(),WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID,campagams.getId());
                Log.d(TAG, "id===: "+campagams.getId());

                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });


        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CardViewtemDecortion());

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 停止自动跳转
        sliderLayout.stopAutoCycle();
    }
}
