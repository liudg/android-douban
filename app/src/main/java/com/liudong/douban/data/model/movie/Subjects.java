package com.liudong.douban.data.model.movie;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.liudong.douban.data.model.Avatars;

import java.util.List;

@AutoValue
public abstract class Subjects implements Parcelable {

    public abstract Rating rating();

    public abstract String title();

    public abstract String year();

    public abstract Avatars images();

    public abstract String id();

    public abstract List<String> genres();

    public static Builder builder() {
        return new AutoValue_Subjects.Builder();
    }

    public static TypeAdapter<Subjects> typeAdapter(Gson gson) {
        return new AutoValue_Subjects.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder rating(Rating rating);

        public abstract Builder title(String title);

        public abstract Builder year(String year);

        public abstract Builder images(Avatars img);

        public abstract Builder id(String id);

        public abstract Builder genres(List<String> genres);

        public abstract Subjects build();

    }
}
