package com.example.fragmenttabhost.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fragmenttabhost.R;
import com.example.fragmenttabhost.bean.Wares;
import com.example.fragmenttabhost.utlis.CartProvider;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Richie on 2017/3/15.
 */

public class ListItemAdapter extends SimpleAdapter<Wares>{

    private CartProvider cartProvider;

    public ListItemAdapter(Context context, List<Wares> list) {
        super(context, R.layout.template_hot_wares,list);
        cartProvider = new CartProvider(context);

    }


    @Override
    protected void convert(BaseViewHolder viewHoder,final Wares item) {


        //商品列表中的图片
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(item.getImgUrl());
        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥"+item.getPrice());

        Button button = viewHoder.getButton(R.id.btn_add);
        if (button!=null){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartProvider.put(item);
                    Toast.makeText(context, "已加入购物车！", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    public  void  resetlayout(int layoutId){
        this.layoutResId = layoutId;
            notifyItemRangeChanged(0,getDatas().size());
    }
}
