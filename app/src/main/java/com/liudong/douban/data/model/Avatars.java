package com.liudong.douban.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Avatars implements Parcelable {

    public abstract String small();

    public abstract String large();

    public abstract String medium();

    public static Builder builder() {
        return new AutoValue_Avatars.Builder();
    }

    public static TypeAdapter<Avatars> typeAdapter(Gson gson) {
        return new AutoValue_Avatars.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder small(String small);

        public abstract Builder large(String large);

        public abstract Builder medium(String medium);

        public abstract Avatars build();

    }
}
