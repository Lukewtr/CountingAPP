package com.example.bandocounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "counter.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_COUNTER = "counter";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String CREATE_TABLE_COUNTER = "CREATE TABLE " +
            TABLE_COUNTER + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COUNT + " INTEGER NOT NULL, " +
            COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COUNTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTER);
        onCreate(db);
    }
}
