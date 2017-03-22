package com.example.fragmenttabhost.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragmenttabhost.R;
import com.example.fragmenttabhost.bean.Campaign;
import com.example.fragmenttabhost.bean.HomeCampaign;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Richie on 2017/3/15.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHodler> {

    public static int VIEW_TYPE_L = 0;
    public static int VIEW_TYPE_R = 1;

    public List<HomeCampaign> mData;

    private LayoutInflater inflater;

    private Context mContext;

    public CampagamsCliclisner mCliclisner;


    public HomeListAdapter(List<HomeCampaign> mList, Context mContext) {
        mData = mList;
        this.mContext = mContext;

    }

    public void setCampagmsCliclinser(CampagamsCliclisner campagmsCliclinser){
       this.mCliclisner = campagmsCliclinser;
    }



    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_R) {
            return new ViewHodler(inflater.inflate(R.layout.template_home_cardview2, parent, false));
        }

        return new ViewHodler(inflater.inflate(R.layout.template_home_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHodler viewholder, int position) {
        HomeCampaign homeCampagams = mData.get(position);

        viewholder.textView_Title.setText(homeCampagams.getTitle());


        Picasso.with(mContext).load(homeCampagams.getCpOne().getImgUrl()).into(viewholder.imageView_Big);
        Picasso.with(mContext).load(homeCampagams.getCpTwo().getImgUrl()).into(viewholder.imageView_SmallTop);
        Picasso.with(mContext).load(homeCampagams.getCpThree().getImgUrl()).into(viewholder.imageView_SmallBottom);

    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : null;
    }


    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return VIEW_TYPE_R;
        } else
            return VIEW_TYPE_L;

    }

    class ViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView_Title;
        private ImageView imageView_Big, imageView_SmallTop, imageView_SmallBottom;


        public ViewHodler(View itemView) {
            super(itemView);

            textView_Title = (TextView) itemView.findViewById(R.id.text_title);
            imageView_Big = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageView_SmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
            imageView_SmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);

            textView_Title.setOnClickListener(this);
            imageView_Big.setOnClickListener(this);
            imageView_SmallBottom.setOnClickListener(this);
            imageView_SmallTop.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mCliclisner!=null){
                anim(v);

            }


        }

        //动画效果
        private void anim(final  View v) {

            ObjectAnimator animator = ObjectAnimator.ofFloat(v,"rotationX",0.0F,360F).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    HomeCampaign campagems = mData.get(getLayoutPosition());

                    switch (v.getId()){
                        case R.id.imgview_big:
                            mCliclisner.onClick(v, campagems.getCpOne());
                            break;

                        case R.id.imgview_small_top:
                            mCliclisner.onClick(v,campagems.getCpTwo());
                            break;

                        case R.id.imgview_small_bottom:
                            mCliclisner.onClick(v,campagems.getCpThree());
                            break;

                        default:
                            break;
                    }



                }
            });

            animator.start();

        }
    }




    public interface CampagamsCliclisner{
        void onClick(View view,Campaign campagams);
    }




}
