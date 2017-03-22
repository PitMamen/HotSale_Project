package com.example.fragmenttabhost.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import com.example.fragmenttabhost.R;
import com.example.fragmenttabhost.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Richie on 2017/3/21.
 *
 *
 * 分类界面右下边的listview适配器
 */

public class WareAdapter extends  SimpleAdapter<Wares> {


    public WareAdapter(Context context,  List<Wares> datas) {
        super(context, R.layout.template_grid_wares, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Wares item) {
        viewHoder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
    }
}
