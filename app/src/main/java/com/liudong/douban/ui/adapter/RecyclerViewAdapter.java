package com.liudong.douban.ui.adapter;

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
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setDate(int position, List<Subjects> subjects) {
        if (position == 0) {
            mData.clear();
        }
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
