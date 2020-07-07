package com.example.myapplication.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class YogaDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "Yoga.db";
    private static final String TABLE_NAME_Mode = "Setting";
    private static final String TABLE_NAME_WorkoutDays = "WorkoutDays";
    private static final String TM_ColMode = "Mode";
    private static final String TW_ColID = "ID";
    private static final String TW_ColDay = "Day";
    private static final int DB_VER = 1;

    public YogaDB(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public int getSettingMode(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        String [] sqlSelect = {"Mode"};
        String sqlTable = "Setting";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex("Mode"));
    }

    public void saveSettingMode (int value){
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("Update Setting SET Mode = %d;", value);
        db.execSQL(query);
    }

    public List<String> getWorkoutDays(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        String [] sqlSelect = {"Day"};
        String sqlTable = "WorkoutDays";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        List<String> result = new ArrayList<>();
        if (c.moveToFirst()){
            do{
                result.add(c.getString(c.getColumnIndex("Day")));
            } while (c.moveToNext());
        }
        return result;
    }

    public void saveDay (String value){
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("Insert into WorkoutDays (Day) Values('%s')", value);
        db.execSQL(query);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_Mode + "(Mode Integer Primary key)");
        db.execSQL("create table " + TABLE_NAME_WorkoutDays + "(ID Integer Primary key Autoincrement,Day Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
