package com.liudong.douban.data.model.movie;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Rating implements Parcelable {

    public abstract int max();

    public abstract double average();

    public abstract String stars();

    public abstract int min();

    public static Builder builder() {
        return new AutoValue_Rating.Builder();
    }

    public static TypeAdapter<Rating> typeAdapter(Gson gson) {
        return new AutoValue_Rating.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder max(int max);

        public abstract Builder average(double average);

        public abstract Builder stars(String star);

        public abstract Builder min(int min);

        public abstract Rating build();
    }
}
