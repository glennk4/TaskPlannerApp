package com.glendall.taskplannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    DatabaseManager db;
    Button addTaskBtn;
    ArrayList<Task> taskList = new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db= new DatabaseManager(this);
        addTaskBtn = (Button) findViewById(R.id.addTaskBtn);

        addTaskBtn.setOnClickListener
                (
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent addTaskIntent = new Intent(MainActivity.this,AddTask.class);
                                startActivity(addTaskIntent);
                            }
                        }
                );

        ViewAll();
    }

    private void ViewAll() {

        LinearLayout  list = (LinearLayout) findViewById(R.id.taskList);
        Cursor result = db.GetAllData();

        if (result.getCount() == 0) {

            Toast.makeText(getApplicationContext(), "No Data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (result.moveToNext()) {
                Task newTask = new Task();
                newTask.id = result.getInt(result.getColumnIndexOrThrow("ID"));
                newTask.taskName = result.getString(result.getColumnIndexOrThrow("NAME"));
                newTask.taskDescription = result.getString(result.getColumnIndexOrThrow("DESCRIPTION"));
                newTask.dueDate = result.getString(result.getColumnIndexOrThrow("DUE"));
                int compBool = result.getInt(result.getColumnIndexOrThrow("COMPLETED"));

                if (compBool == 1)
                {
                    newTask.completed = -1;
                }
                else
                {
                    newTask.completed = 0;
                }
                newTask.doneDate = result.getString(result.getColumnIndexOrThrow("DONE_DATE"));
                taskList.add(newTask);
            }
            Button taskBtn[] = new Button[taskList.size()];
            for (int i = 0; i < taskList.size(); i++) {
                taskBtn[i] = new Button(this);
                list.addView(taskBtn[i]);
                taskBtn[i].setText(taskList.get(i).getTaskName());
                taskBtn[i].setId(i);
                taskBtn[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, 82);
                taskBtn[i].setTextColor(Color.BLACK);
                taskBtn[i].setTextSize(LinearLayout.LayoutParams.WRAP_CONTENT);


                taskBtn[i].setBackgroundColor(Color.TRANSPARENT);
                if (taskList.get(i).completed==-1) {
                    taskBtn[i].setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }

                taskBtn[i].setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int buttonId = v.getId();
                                // viewTask(taskList, buttonId);
                            }
                        }
                );
                DisplayList.ViewAll(list, result, taskList);
            }

        }
    }

}