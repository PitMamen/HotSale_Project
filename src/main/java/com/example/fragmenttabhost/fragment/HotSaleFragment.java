package com.example.fragmenttabhost.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.example.fragmenttabhost.Contants;
import com.example.fragmenttabhost.R;
import com.example.fragmenttabhost.WareDetailActivity;
import com.example.fragmenttabhost.adapter.BaseAdapter;
import com.example.fragmenttabhost.adapter.ListItemAdapter;
import com.example.fragmenttabhost.bean.Page;
import com.example.fragmenttabhost.bean.Wares;
import com.example.fragmenttabhost.utlis.Pager;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Richie on 2017/3/14.
 */

public class HotSaleFragment extends BaseFragment implements Pager.OnPageListener {
    @ViewInject(R.id.recyclerview_hot)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.hotrefresh_view)
    private MaterialRefreshLayout materialRefreshLayout;

    private ListItemAdapter mListItemAdapter;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hotsalefragment_layout, container, false);

    }

    @Override
    public void init() {

        Pager pager = Pager.newBuilder()
                .setPageSize(20)
                .setOnPageListener(this)
                .setRefreshLayout(materialRefreshLayout)
                .setUrl(Contants.API.WARES_HOT)
                .setLoadMore(true)
                .build(getActivity(), new TypeToken<Page<Wares>>() {}.getType());

          pager.request();
                }


    @Override
    public void load(final List datas, int totalPage, int totalCount) {
           if (mListItemAdapter==null){
               mListItemAdapter = new ListItemAdapter(getActivity(),datas);
               mListItemAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                   @Override
                   public void onItemClick(View view, int position) {
                       Wares wares = (Wares) datas.get(position);
                       Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                       intent.putExtra(Contants.WARE,wares);
                       startActivity(intent);


                   }
               });

               mRecyclerView.setAdapter(mListItemAdapter);
               mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
               mRecyclerView.setItemAnimator(new DefaultItemAnimator());

           }


    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
            mListItemAdapter.refreshData(datas);    //刷新数据
            mRecyclerView.scrollToPosition(0);


    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {

         mListItemAdapter.loadMoreData(datas);  //加载更多
        mRecyclerView.scrollToPosition(mListItemAdapter.getDatas().size());  //加载更多时当前页数据

    }
}
