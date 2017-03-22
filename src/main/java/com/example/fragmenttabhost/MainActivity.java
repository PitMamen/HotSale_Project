package com.example.fragmenttabhost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.fragmenttabhost.bean.Tab;
import com.example.fragmenttabhost.fragment.CatergroyFragment;
import com.example.fragmenttabhost.fragment.HomeFragment;
import com.example.fragmenttabhost.fragment.HotSaleFragment;
import com.example.fragmenttabhost.fragment.MineFragment;
import com.example.fragmenttabhost.fragment.ShoppingFragment;
import com.example.fragmenttabhost.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FragmentTabHost tabHost;
    private LayoutInflater inflater;
    private List<Tab> tabList = new ArrayList<>(5);

    ShoppingFragment shopfreagment =  new ShoppingFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initTab();
    }

    private void initTab() {
        Tab tabhome = new Tab(R.string.home,HomeFragment.class,R.drawable.selector_icon_home);
        Tab tabhot = new Tab(R.string.hot,HotSaleFragment.class,R.drawable.selector_icon_hot);
        Tab tabcatergroy = new Tab(R.string.catagory,CatergroyFragment.class,R.drawable.selector_icon_category);
        Tab tabcar = new Tab(R.string.cart,ShoppingFragment.class,R.drawable.selector_icon_cart);
        Tab tabmine = new Tab(R.string.mine,MineFragment.class,R.drawable.selector_icon_mine);

        tabList.add(tabhome);
        tabList.add(tabhot);
        tabList.add(tabcatergroy);
        tabList.add(tabcar);
        tabList.add(tabmine);
        //实例化布局对象
        inflater = LayoutInflater.from(this);

        tabHost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);   //必须要用android 自带的id
        tabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
         //循环添加
        for (Tab tab : tabList){
            //tab按钮添加文字和图片
           TabHost.TabSpec tabspec =  tabHost.newTabSpec(getString(tab.getTitle()));
            tabspec.setIndicator(buildIndicator(tab));
            //添加fragment
            tabHost.addTab(tabspec,tab.getFragment(),null);
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId==getString(R.string.cart)){
                    refData();

                }
            }

        });
           //去除分割线
          tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
          tabHost.setCurrentTab(0);  //设置当前默认的tab

    }

    private void refData() {
        if (shopfreagment==null){
            android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (f!=null){
                shopfreagment = (ShoppingFragment) f;
            }
        }

    }

    private View buildIndicator(Tab tab) {
        View view = inflater.inflate(R.layout.tab_indicator,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView txt  = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        txt.setText(tab.getTitle());
        return view;
    }
}
