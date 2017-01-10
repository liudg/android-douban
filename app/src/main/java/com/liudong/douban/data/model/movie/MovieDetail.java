package com.liudong.douban.data.model.movie;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.liudong.douban.data.model.Avatars;

import java.util.List;

@AutoValue
public abstract class MovieDetail implements Parcelable {

    public abstract Rating rating();

    public abstract String year();

    public abstract Avatars images();

    public abstract String id();

    public abstract String mobile_url();

    public abstract String title();

    public abstract String share_url();

    public abstract int collect_count();

    public abstract String original_title();

    public abstract String summary();

    public abstract List<String> countries();

    public abstract List<String> genres();

    public abstract List<Actors> casts();

    public abstract List<Actors> directors();

    public abstract List<String> aka();

    public static Builder builder() {
        return new AutoValue_MovieDetail.Builder();
    }

    public static TypeAdapter<MovieDetail> typeAdapter(Gson gson) {
        return new AutoValue_MovieDetail.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder rating(Rating rating);

        public abstract Builder year(String year);

        public abstract Builder images(Avatars img);

        public abstract Builder id(String id);

        public abstract Builder mobile_url(String url);

        public abstract Builder title(String title);

        public abstract Builder share_url(String url);

        public abstract Builder collect_count(int count);

        public abstract Builder original_title(String title);

        public abstract Builder summary(String summary);

        public abstract Builder countries(List<String> countries);

        public abstract Builder genres(List<String> genres);

        public abstract Builder casts(List<Actors> casts);

        public abstract Builder directors(List<Actors> directors);

        public abstract Builder aka(List<String> aka);

        public abstract MovieDetail build();
    }
}
