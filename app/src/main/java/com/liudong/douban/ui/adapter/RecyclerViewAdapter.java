package com.liudong.douban.ui.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liudong.douban.R;
import com.liudong.douban.data.model.movie.Subjects;
import com.liudong.douban.ui.activity.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Subjects> mData = new ArrayList<>();
    private Fragment fragment;

    public RecyclerViewAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Subjects subjects = mData.get(position);
        String intro = "";
        for (String genre : subjects.genres()) {
            intro += genre + "/";
        }
        intro += subjects.year();
        Glide.with(fragment)
                .load(subjects.images().medium())
                .placeholder(R.mipmap.placeholder)
                .into(holder.iv_avatar);
        holder.tv_title.setText(subjects.title());
        holder.rb_start.setNumStars(subjects.rating().max() / 2);
        holder.rb_start.setRating((float) subjects.rating().average() / 2);
        holder.tv_rating.setText(String.valueOf(subjects.rating().average()));
        holder.tv_cast.setText(intro);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragment.getContext(), MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("imgUrl", subjects.images().large());
                bundle.putString("title", subjects.title());
                bundle.putString("id", subjects.id());
                intent.putExtra("data", bundle);
                fragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setDate(List<Subjects> subjects) {
        mData.clear();
        mData.addAll(subjects);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.iv_avatar)
        ImageView iv_avatar;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.rb_start)
        RatingBar rb_start;
        @BindView(R.id.tv_rating)
        TextView tv_rating;
        @BindView(R.id.tv_cast)
        TextView tv_cast;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
