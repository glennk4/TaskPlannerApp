package com.glendall.taskplannerapp;
/*
    Establishes SQL Connection
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseManager extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "ToDo.db";
    public static final String TABLE_NAME  = "task_table";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "DESCRIPTION";
    public static final String COL_4 = "DUE";
    public static final String COL_5 = "COMPLETED";
    public static final String COL_6 = "DONE_DATE";

    public DatabaseManager(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT," +
                "DESCRIPTION TEXT,DUE DATE, COMPLETED, DONE_DATE) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean InsertData(String name, String description, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, description);
        contentValues.put(COL_4, dueDate);
        contentValues.put(COL_5, 0);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public Cursor GetAllData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY due",null);
    }

    public Cursor GetTaskData(int taskId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE ID=="+taskId,null);
    }

    public  boolean UpdateData(String name, String description, String due, Boolean completed, String done_date )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,description);
        contentValues.put(COL_4,due);
        contentValues.put(COL_5,completed);
        contentValues.put(COL_6,done_date);

        long result = db.insert(TABLE_NAME,null, contentValues);

        return result != -1;
    }

    public boolean DeleteTask(int taskId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,"ID=="+taskId, null);

        return result != -1;
    }

    public boolean CompleteTask(int taskId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault().format);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        contentValues.put(COL_5,1);
        contentValues.put(COL_6,date.toString());
        long result = db.update(TABLE_NAME,contentValues,"ID=="+taskId,null);
        return result != -1;
    }

    public boolean EditTask(String name, String description, String due, int taskId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,description);
        contentValues.put(COL_4,due);
        long result = db.update(TABLE_NAME, contentValues,"ID=="+taskId,null);

        return result != -1;
    }
}