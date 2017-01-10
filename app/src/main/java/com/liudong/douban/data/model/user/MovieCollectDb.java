package com.liudong.douban.data.model.user;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

/**
 * Created by liudong on 2017/1/6.
 * 收藏信息保存到数据库，不需要每次去服务器读取判断是否收藏
 */

@AutoValue
public abstract class MovieCollectDb implements MovieCollectDbModel, Parcelable {
    public static final Factory<MovieCollectDb> FACTORY = new Factory<>(new MovieCollectDbModel.Creator<MovieCollectDb>() {
        @Override
        public MovieCollectDb create(@NonNull String objectId, @NonNull String mid) {
            return new AutoValue_MovieCollectDb(objectId, mid);
        }
    });

    public static final RowMapper<MovieCollectDb> MAPPER = FACTORY.select_rowMapper();

    public static Builder builder() {
        return new AutoValue_MovieCollectDb.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder objectId(String objectId);

        public abstract Builder mid(String id);

        public abstract MovieCollectDb build();
    }
}
