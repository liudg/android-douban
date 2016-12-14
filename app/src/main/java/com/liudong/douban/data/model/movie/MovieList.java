package com.liudong.douban.data.model.movie;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

@AutoValue
public abstract class MovieList implements Parcelable {

    public abstract int count();

    public abstract int start();

    public abstract int total();

    public abstract String title();

    public abstract List<Subjects> subjects();

    public static Builder builder() {
        return new AutoValue_MovieList.Builder();
    }

    public static TypeAdapter<MovieList> typeAdapter(Gson gson) {
        return new AutoValue_MovieList.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder count(int count);

        public abstract Builder start(int start);

        public abstract Builder total(int total);

        public abstract Builder title(String title);

        public abstract Builder subjects(List<Subjects> subjects);

        public abstract MovieList build();
    }
}
