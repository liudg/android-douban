package com.liudong.douban.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liudong.douban.R;
import com.liudong.douban.data.model.user.MovieCollect;
import com.liudong.douban.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.MyViewHolder> {
    private List<MovieCollect> mData = new ArrayList<>();
    private Fragment fragment;

    public CollectAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setDate(int position, List<MovieCollect> date) {
        if (position == 0) {
            mData.clear();
        }
        mData.addAll(date);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_collect, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MovieCollect collect = mData.get(position);
        Glide.with(fragment)
                .load(collect.getImgUrl())
                .placeholder(R.mipmap.placeholder)
                .into(holder.ivAvatar);
        holder.tvTitle.setText(collect.getTitle());
        holder.tvActor.setText("" + "演员:" + collect.getActor());
        holder.tvYear.setText("" + "年份:" + collect.getYear());
        holder.tvTime.setText(TimeUtil.getShortTime(collect.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return mData.size() == 0 ? 0 : mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_actor)
        TextView tvActor;
        @BindView(R.id.tv_year)
        TextView tvYear;
        @BindView(R.id.tv_time)
        TextView tvTime;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
