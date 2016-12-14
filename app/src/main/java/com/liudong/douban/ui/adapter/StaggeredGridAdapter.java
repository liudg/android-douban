package com.liudong.douban.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liudong.douban.R;
import com.liudong.douban.data.model.movie.Actors;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.MyViewHolder> {

    private List<Actors> mData;
    private Activity activity;

    public StaggeredGridAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.actor_info, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Actors actors = mData.get(position);
        Glide.with(activity)
                .load(actors.avatars().medium())
                .placeholder(R.mipmap.placeholder)
                .into(holder.iv_avatar);
        holder.tv_name.setText(actors.name());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(List<Actors> actors) {
        mData = actors;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView iv_avatar;
        @BindView(R.id.tv_name)
        TextView tv_name;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
