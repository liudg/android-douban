package com.liudong.douban.data.model.movie;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

@AutoValue
public abstract class Subjects implements Parcelable {

    public abstract int max();

    public abstract double average();

    public abstract String title();

    public abstract String year();

    public abstract String medium();

    public abstract String id();

    public abstract List<String> genres();

    public abstract List<Actors> casts();

    public abstract List<Actors> directors();

    public static Builder builder() {
        return new AutoValue_Subjects.Builder();
    }

    public static TypeAdapter<Subjects> typeAdapter(Gson gson) {
        return new AutoValue_Subjects.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder max(int max);

        public abstract Builder average(double average);

        public abstract Builder title(String title);

        public abstract Builder year(String year);

        public abstract Builder medium(String img);

        public abstract Builder id(String id);

        public abstract Builder genres(List<String> genres);

        public abstract Builder casts(List<Actors> casts);

        public abstract Builder directors(List<Actors> directors);

        public abstract Subjects build();

    }
}
