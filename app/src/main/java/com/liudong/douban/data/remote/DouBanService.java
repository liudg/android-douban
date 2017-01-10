package com.liudong.douban.data.remote;

import com.liudong.douban.data.model.movie.MovieDetail;
import com.liudong.douban.data.model.movie.MovieList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 如果不需要任何类型安全的响应，你可以把返回值指定为Call<ResponseBody>，Observable<ResponseBody>
 */
public interface DouBanService {

    String BaseUrl = "https://api.douban.com/v2/";

    /**
     * 正在热映
     */
    @GET("movie/in_theaters")
    Observable<MovieList> getHotMovie(@Query("start") int start, @Query("count") int count);

    /**
     * Top250
     */
    @GET("movie/top250")
    Observable<MovieList> getTopMovie(@Query("start") int start, @Query("count") int count);

    /**
     * 即将上映
     */
    @GET("movie/coming_soon")
    Observable<MovieList> getUpcomingMovie(@Query("start") int start, @Query("count") int count);

    /**
     * 电影详情
     */
    @GET("movie/subject/{id}")
    Observable<MovieDetail> getMovieDetail(@Path("id") int id);
}
