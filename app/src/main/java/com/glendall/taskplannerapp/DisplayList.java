package com.glendall.taskplannerapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class DisplayList
{
    @SuppressLint("Range")
    public static void ViewAll(LinearLayout list, Cursor result, ArrayList<Task> taskList)
    {
        if(result.getCount()>0)
        {
            while (result.moveToNext())
            {
                Task newTask = new Task();
                newTask.id = result.getInt(result.getColumnIndex("ID"));
                newTask.taskName = result.getString(result.getColumnIndex("TASKNAME"));
                newTask.taskDescription = result.getString(result.getColumnIndex("TASKDESCRIPTION"));
                newTask.dueDate = result.getString(result.getColumnIndex("DUEDATE"));
                newTask.doneDate = result.getString(result.getColumnIndex("DONEDATE"));
                newTask.completed = result.getInt(result.getColumnIndex("COMPLETED"));
                newTask.startDate = result.getString(result.getColumnIndex("STARTDATE"));
                taskList.add(newTask);
            }
        }
    }
}
