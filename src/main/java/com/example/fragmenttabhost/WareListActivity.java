package com.example.fragmenttabhost;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.example.fragmenttabhost.adapter.BaseAdapter;
import com.example.fragmenttabhost.adapter.decoration.DividerItemDecoration;
import com.example.fragmenttabhost.adapter.ListItemAdapter;
import com.example.fragmenttabhost.bean.Page;
import com.example.fragmenttabhost.bean.Wares;
import com.example.fragmenttabhost.utlis.Pager;
import com.example.fragmenttabhost.widget.CNiaoToolBar;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 *  商品列表activity
 */
public class WareListActivity extends AppCompatActivity  implements Pager.OnPageListener<Wares>,TabLayout.OnTabSelectedListener,View.OnClickListener {

    private static final String TAG = "WareListActivity";

    public static final int TAG_DEFAULT=0;
    public static final int TAG_SALE=1;
    public static final int TAG_PRICE=2;

    public static final int ACTION_LIST=1;
    public static final int ACTION_GIRD=2;




    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;

    @ViewInject(R.id.txt_summary)
    private TextView mTxtSummary;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview_wares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;    //自定义下拉刷新控件

    @ViewInject(R.id.toobar)
    private CNiaoToolBar mToolbar;


    private int orderBy = 0;
    private long campaignId = 0;


    private ListItemAdapter mWaresAdapter;
    private   Pager pager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.warelistactivity_layout);
        ViewUtils.inject(this);

        initToolBar();

        campaignId=getIntent().getLongExtra(Contants.COMPAINGAIN_ID,0);

       initTab();

        getData();


    }

       // 初始化toobar
    private void initToolBar(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });


        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);  //设置默认
        mToolbar.getRightButton().setTag(ACTION_LIST);   //设置默认的tag为  ACTION_LIST


        mToolbar.setRightButtonOnClickListener(this);


    }

           //获取数据
    private void getData(){


        pager= Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId",campaignId)
                .putParam("orderBy",orderBy)  //传入获取的tag 以得到不同的数据
                .setRefreshLayout(mRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this,new TypeToken<Page<Wares>>(){}.getType());

        pager.request();  // 请求数据

    }


      //初始化 tab
    private void initTab(){
        mTablayout.addTab(mTablayout.newTab().setText("默认").setTag(TAG_DEFAULT));

        mTablayout.addTab(mTablayout.newTab().setText("价格").setTag(TAG_PRICE));

        mTablayout.addTab(mTablayout.newTab().setText("销量").setTag(TAG_SALE));


        mTablayout.setOnTabSelectedListener(this);   //给mTablayout注册注册监听
    }

    //pager类实现的方法
    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {

        mTxtSummary.setText("共有"+totalCount+"件商品");

        if (mWaresAdapter == null) {
            mWaresAdapter = new ListItemAdapter(this, datas);
            mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Wares wares = mWaresAdapter.getItem(position);

                    Intent intent = new Intent(WareListActivity.this, WareDetailActivity.class);

                    intent.putExtra(Contants.WARE,wares);  //传ware对象
                    startActivity(intent);
                }
            });
            mRecyclerview_wares.setAdapter(mWaresAdapter);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview_wares.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
            mRecyclerview_wares.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }

    }
    //pager类实现的方法
    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {

        mWaresAdapter.refreshData(datas);   //刷新数据
        mRecyclerview_wares.scrollToPosition(0);
    }
    //pager类实现的方法
    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);   //加载更多
    }





    //OnTabSelectedListener实现的方法
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        orderBy = (int) tab.getTag();  // 点击 切换不同tag
        pager.putParam("orderBy",orderBy);
        pager.request();
    }
    //OnTabSelectedListener实现的方法
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }
    //OnTabSelectedListener实现的方法
    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();

        if(ACTION_LIST == action){    //点击默认的tag时 发生改变

            mToolbar.setRightButtonIcon(R.drawable.icon_list_32);    //图标改变
            mToolbar.getRightButton().setTag(ACTION_GIRD);       //重新設置tag
            mWaresAdapter.resetlayout(R.layout.template_grid_wares);   //  布局改变
            mRecyclerview_wares.setLayoutManager(new GridLayoutManager(this,2));

        }
        else if(ACTION_GIRD == action){
            mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
            mToolbar.getRightButton().setTag(ACTION_LIST);

            mWaresAdapter.resetlayout(R.layout.template_hot_wares);

            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
        }

    }
}
