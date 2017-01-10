package com.liudong.douban.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liudong.douban.data.model.user.MovieCollect;
import com.liudong.douban.data.model.user.MovieCollectDb;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Singleton
public class DataBaseHelper {

    private final BriteDatabase mDb;

    @Inject
    DataBaseHelper(DbOpenHelper dbOpenHelper) {
        SqlBrite.Builder briteBuilder = new SqlBrite.Builder();
        mDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    /**
     * 插入单条电影数据
     */
    public void insertMovie(MovieCollectDb data) {
        mDb.insert(MovieCollectDb.TABLE_NAME, MovieCollectDb.FACTORY.marshal(data).asContentValues(),
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 批量插入电影数据
     */
    public void insertMovieList(List<MovieCollect> list) {
        if (list != null) {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                for (MovieCollect data : list) {
                    mDb.insert(MovieCollectDb.TABLE_NAME,
                            MovieCollectDb.FACTORY.marshal(MovieCollectDb.builder()
                                    .objectId(data.getObjectId())
                                    .mid(data.getId())
                                    .build()).asContentValues()
                            , SQLiteDatabase.CONFLICT_REPLACE);
                }
                transaction.markSuccessful();
            } finally {
                transaction.end();
            }
        }
    }

    /**
     * 删除电影数据
     */
    public void deleteMovie(String objectId) {
        mDb.delete(MovieCollectDb.TABLE_NAME, MovieCollectDb.OBJECTID + "=?", objectId);
    }

    /**
     * 删除所有电影
     */
    public void deleteAllMovie() {
        mDb.execute(MovieCollectDb.DELETE_ALL);
    }

    /**
     * 查询电影数据
     */
    public Observable<MovieCollectDb> queryMovie(String id) {
        return mDb.createQuery(MovieCollectDb.TABLE_NAME, MovieCollectDb.SELECT_ROW, id)
                .map(new Func1<SqlBrite.Query, MovieCollectDb>() {
                    @Override
                    public MovieCollectDb call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        try {
                            if (!cursor.moveToNext()) {
                                return null;
                            } else {
                                return MovieCollectDb.MAPPER.map(cursor);
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                });
    }

    /**
     * 查询电影总条数
     */
    public Observable<Integer> queryCount() {
        return mDb.createQuery(MovieCollectDb.TABLE_NAME, MovieCollectDb.SELECT_COUNT)
                .mapToOne(new Func1<Cursor, Integer>() {
                    @Override
                    public Integer call(Cursor cursor) {
                        return cursor.getInt(0);
                    }
                });
    }
}
