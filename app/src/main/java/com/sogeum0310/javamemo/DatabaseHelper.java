package com.sogeum0310.javamemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.sogeum0310.javamemo.MemoData.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databasename = "MEMO.db";
    public static final String tablename = "memo";
    public static final int dbVer = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, databasename, null, dbVer);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Memolist.tablename + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+ Memolist.content +" TEXT , " +
                Memolist.date + " int , " +
                Memolist.feel+" INT, " +
                Memolist.arlam + " int);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table "+ Memolist.tablename);
        onCreate(db);
    }
}
