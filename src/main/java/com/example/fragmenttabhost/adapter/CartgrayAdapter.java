package com.example.fragmenttabhost.adapter;

import android.content.Context;

import com.example.fragmenttabhost.R;
import com.example.fragmenttabhost.bean.Category;

import java.util.List;

/**
 * Created by Pengxinkai on 2017/3/20.
 */

public class CartgrayAdapter extends  SimpleAdapter<Category> {


    public CartgrayAdapter(Context context,  List<Category> datas) {
        super(context, R.layout.template_simple_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {
        viewHoder.getTextView(R.id.tv_cartgray).setText(item.getName());
    }
}
