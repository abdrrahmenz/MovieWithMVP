package com.expert.andro.basisdatacataloguemovie.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.expert.andro.basisdatacataloguemovie.data.provider.MovieDbContract.Movie.TABLE_NAME;

/**
 * Created by adul on 27/09/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "popularmovie.db";
    private static final int DB_VERSION = 1;

    private Context context;

    private String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
            + MovieDbContract.Movie.COLUMN_MOVIE_ID + " TEXT NOT NULL,"
            + MovieDbContract.Movie.COLUMN_MOVIE_TITLE + " TEXT NOT NULL,"
            + MovieDbContract.Movie.COLUMN_MOVIE_OVERVIEW + " TEXT,"
            + MovieDbContract.Movie.COLUMN_MOVIE_BACKDROP_PATH + " TEXT,"
            + MovieDbContract.Movie.COLUMN_MOVIE_POSTER_PATH + " TEXT,"
            + MovieDbContract.Movie.COLUMN_MOVIE_RELEASE_DATE + " TEXT,"
            + MovieDbContract.Movie.COLUMN_MOVIE_FAVORED + " INTEGER NOT NULL DEFAULT 0,"
            + "UNIQUE (" + MovieDbContract.Movie.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP IF TABLE EXISTS "+DB_NAME);
        onCreate(sqLiteDatabase);
    }


    /**
     * Delete the whole database.
     */
    void deleteDatabase() {
        context.deleteDatabase(DB_NAME);
    }
}
