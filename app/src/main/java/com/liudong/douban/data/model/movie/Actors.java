package com.liudong.douban.data.model.movie;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.liudong.douban.data.model.Avatars;

@AutoValue
public abstract class Actors implements Parcelable {

    public abstract String alt();

    public abstract Avatars avatars();

    public abstract String name();

    public abstract String id();

    public static Builder builder() {
        return new AutoValue_Actors.Builder();
    }

    public static TypeAdapter<Actors> typeAdapter(Gson gson) {
        return new AutoValue_Actors.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder alt(String alt);

        public abstract Builder avatars(Avatars img);

        public abstract Builder name(String name);

        public abstract Builder id(String id);

        public abstract Actors build();
    }
}
