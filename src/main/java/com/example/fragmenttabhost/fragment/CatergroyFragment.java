package com.example.fragmenttabhost.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.example.fragmenttabhost.Contants;
import com.example.fragmenttabhost.R;
import com.example.fragmenttabhost.WareDetailActivity;
import com.example.fragmenttabhost.adapter.BaseAdapter;
import com.example.fragmenttabhost.adapter.CartgrayAdapter;
import com.example.fragmenttabhost.adapter.decoration.DividerItemDecoration;
import com.example.fragmenttabhost.adapter.WareAdapter;
import com.example.fragmenttabhost.bean.Banner;
import com.example.fragmenttabhost.bean.Category;
import com.example.fragmenttabhost.bean.Page;
import com.example.fragmenttabhost.bean.Wares;
import com.example.fragmenttabhost.utlis.BaseCallback;
import com.example.fragmenttabhost.utlis.OkHttpHelper;
import com.example.fragmenttabhost.utlis.SpotsCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * Created by Richie on 2017/3/14.
 */

public class CatergroyFragment extends BaseFragment {
    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mText_RecyclerView;

    @ViewInject(R.id.banner_slider)
    private SliderLayout slidlayout;

    @ViewInject(R.id.list_WarelistView)
    private RecyclerView mList_WareRecyclerview;
    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout refreshlayout;

    private CartgrayAdapter mCatgayAdaper;

    private WareAdapter mWareAdapter;

    private OkHttpHelper okhttp = OkHttpHelper.getInstance();

    private int currPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private long category_id = 0;


    private static final int STATE_NORMAL = 0;  //正常模式
    private static final int STATE_REFREH = 1;  //刷新模式
    private static final int STATE_MORE = 2;      //加载更多

    private int state = STATE_NORMAL;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.catergroyfragment_layout, container, false);
        return view;
    }

    @Override
    public void init() {
        // 左边侧边栏listview
        requestCategoryData();
        //右边广告界面
        requestBannerData();
        //初始化刷新组件
        initRefreshLayout();
    }


    //请求分类数据  左边的文字
    public void requestCategoryData() {
        okhttp.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);

                if (categories.size() > 0 && categories != null) {
                    category_id = categories.get(0).getId();

                    //根据给的ID 显示不同的界面信息
                    requestWare(category_id);

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }


    //显示分类信息  左边的文字
    public void showCategoryData(List<Category> mList) {

        if (mCatgayAdaper == null) {
            mCatgayAdaper = new CartgrayAdapter(getActivity(), mList);
            mCatgayAdaper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Category category = mCatgayAdaper.getItem(position);

                    category_id = category.getId();
                    currPage = 1;
                    state = STATE_NORMAL;

                    //数据请求
                    requestWare(category_id);

                }
            });

            mText_RecyclerView.setAdapter(mCatgayAdaper);
            mText_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mText_RecyclerView.setItemAnimator(new DefaultItemAnimator());
            mText_RecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        }

    }


    //请求Ware数据
    private void requestWare(long category_id) {
        String url = Contants.API.WARES_LIST + "?categoryId=" + category_id + "&curPage=" + currPage + "&pageSize=" + pageSize;
        okhttp.get(url, new BaseCallback<Page<Wares>>() {
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
            public void onSuccess(Response response, Page<Wares> waresPage) {

                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();

                //显示实体信息操作
                showWareData(waresPage.getList());

            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    //显示下面listview数据
    private void showWareData(final List<Wares> warelist) {
        switch (state) {
            case STATE_NORMAL:
                if (mWareAdapter == null) {
                    mWareAdapter = new WareAdapter(getContext(), warelist);
                    mWareAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Wares ware = warelist.get(position);
                            Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                            intent.putExtra(Contants.WARE, ware);
                            startActivity(intent);
                        }
                    });
                    mList_WareRecyclerview.setAdapter(mWareAdapter);
                    mList_WareRecyclerview.setItemAnimator(new DefaultItemAnimator());
                    mList_WareRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
                } else {
                    mWareAdapter.clear();
                    mWareAdapter.addData(warelist);
                }
                break;

            case STATE_REFREH:
                mWareAdapter.clear();
                mWareAdapter.addData(warelist);

                mList_WareRecyclerview.scrollToPosition(0);
                refreshlayout.finishRefresh();
                break;

            case STATE_MORE:
                mWareAdapter.addData(mWareAdapter.getDatas().size(), warelist);
                mList_WareRecyclerview.scrollToPosition(mCatgayAdaper.getDatas().size());

                refreshlayout.finishRefreshLoadMore();
                break;


        }

    }

    //请求右边广告数据
    private void requestBannerData() {
        String url = Contants.API.BANNER + "?type=1";
        okhttp.get(url, new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banerlist) {

                showSliderBanner(banerlist);


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //显示广告栏
    private void showSliderBanner(List<Banner> banerlist) {
        if (banerlist != null) {
            for (Banner banner : banerlist) {
                DefaultSliderView defaultsliderview = new DefaultSliderView(getContext());
                defaultsliderview.description(banner.getName());
                defaultsliderview.image(banner.getImgUrl());
                defaultsliderview.setScaleType(BaseSliderView.ScaleType.Fit);
                slidlayout.addSlider(defaultsliderview);    //添加视图
            }
        }
        slidlayout.setCustomAnimation(new DescriptionAnimation());
        slidlayout.setPresetIndicator(SliderLayout.PresetIndicators.Left_Bottom);   //广告栏下面的点  导航栏
        slidlayout.setPresetTransformer(SliderLayout.Transformer.Default);
        slidlayout.setDuration(2000);   //2秒跳转

    }

      //初始化下拉刷新控件
    private void initRefreshLayout() {
        refreshlayout.setLoadMore(true);
        refreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }


            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (currPage <= totalPage) {
                    loadMoreData();
                }
                else {
                    Toast.makeText(getContext(), "无内容可加载", Toast.LENGTH_SHORT).show();
                    materialRefreshLayout.finishRefreshLoadMore();
                }

            }
        });
    }

    //刷新
    private void refreshData() {
        currPage = 1;
        state = STATE_REFREH;
        requestWare(category_id);   //加载
    }

    //加载更多
    private void loadMoreData() {
        currPage = ++currPage;
        state = STATE_MORE;
        requestWare(category_id);   //加载

    }

}
