package com.example.fragmenttabhost;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.fragmenttabhost.bean.Wares;
import com.example.fragmenttabhost.utlis.CartProvider;
import com.example.fragmenttabhost.widget.CNiaoToolBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

/**
 * Created by Richie on 2017/3/15.
 */
public class WareDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private  final String TAG =getClass().getSimpleName()  ;
    @ViewInject(R.id.detail_toobar)
    private CNiaoToolBar toolBar;

    @ViewInject(R.id.detail_webview)
    private WebView mWebview;

    private CartProvider cartProvider;

     //动态dialog
    private SpotsDialog spotsDialog;

    private Wares mWares;

    //JavaScript接口
    private  WebAppInterface mWebinterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wareldetailistactivity_layout);
        ViewUtils.inject(this);
        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);
        if (serializable==null){
            this.finish();
        }

        //  页面加载时出现的动态左右dialog
        spotsDialog = new SpotsDialog(this,"loading.....");
        spotsDialog.show();

        mWares = (Wares) serializable;

        Log.d(TAG, "Wares==: "+mWares);

        cartProvider = new CartProvider(this);
        initToolBar();
        initWebView();




    }

    private void initWebView() {

        WebSettings websetting  = mWebview.getSettings();
        websetting.setAppCacheEnabled(true);
        websetting.setJavaScriptEnabled(true);
        websetting.setBlockNetworkImage(false);

        mWebview.loadUrl(Contants.API.WARES_DETAIL);
         //JavaScript接口实例化
        mWebinterface = new WebAppInterface(this);
      //给webview 添加JavaScript接口
        mWebview.addJavascriptInterface(mWebinterface,"mWebinterface");
        mWebview.setWebViewClient(new Finshactivity());
    }

    private void initToolBar(){
        toolBar.setNavigationOnClickListener(this);
        toolBar.setRightButtonText("分享");

        toolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showShare();
            }
        });

    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));

        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.cniao5.com");

        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWares.getName());

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(mWares.getImgUrl());

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.cniao5.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(mWares.getName());

        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.google.com");

// 启动分享GUI
        oks.show(this);
    }


    @Override
    public void onClick(View v) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ShareSDK.stopSDK(this);

    }


    class Finshactivity extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (spotsDialog!=null && spotsDialog.isShowing()){
                  spotsDialog.dismiss();
            }

             mWebinterface.showDetail();

        }
    }



      //创建JavaScript接口
  class  WebAppInterface{
      private  Context mContext;
      public WebAppInterface(Context con){
          this.mContext = con;
      }
    @JavascriptInterface
      public void  showDetail(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //  "javascript:showDetail("  ")"  渲染作用
                mWebview.loadUrl("javascript:showDetail("+mWares.getId()+")");

            }
        });
      }

      @JavascriptInterface
      public  void  buy(long id){
          cartProvider.put(mWares);
          Toast.makeText(mContext, "已添加至购物车！", Toast.LENGTH_SHORT).show();
      }

  }



}
