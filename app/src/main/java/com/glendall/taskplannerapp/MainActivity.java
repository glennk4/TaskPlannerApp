package com.glendall.taskplannerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    DatabaseManager db;
    Button addTaskBtn;
    ArrayList<Task> taskList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db= new DatabaseManager(this);
        addTaskBtn = findViewById(R.id.addTaskBtn);

        addTaskBtn.setOnClickListener
                (
                        view -> {
                            Intent addTaskIntent = new Intent(MainActivity.this,AddTask.class);
                            startActivity(addTaskIntent);
                        }
                );

        ViewAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ViewAll()
    {
        LinearLayout  list = findViewById(R.id.taskList);
        Cursor result = db.GetAllData();

        if (result.getCount() == 0)
        {
            Toast.makeText(getApplicationContext(), "No Data to show", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while (result.moveToNext())
            {
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
            Button[] taskBtn = new Button[taskList.size()];

            for (int i = 0; i < taskList.size(); i++)
            {
                taskBtn[i] = new Button(this);
                list.addView(taskBtn[i]);
                taskBtn[i].setText(taskList.get(i).getTaskName());
                taskBtn[i].setId(i);
                taskBtn[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, 64);
                ViewGroup.LayoutParams params = list.getLayoutParams();
                params.height = params.height +100;

                if(i  % 2 == 0)
                {
                    taskBtn[i].setTextColor(Color.GREEN);
                }
                else {
                    taskBtn[i].setTextColor(Color.BLACK);
                }
                taskBtn[i].setBackgroundColor(Color.TRANSPARENT);
                taskBtn[i].setTextAppearance(R.style.fontForHomePage);

                if (taskList.get(i).completed==-1)
                {
                    taskBtn[i].setTextColor(Color.GREEN);
                }
                else
                {
                    taskBtn[i].setTextColor(Color.BLACK);
                }

                taskBtn[i].setOnClickListener(
                        v -> {
                            int buttonId = v.getId();
                            ViewTask(taskList, buttonId);
                        }
                );

            }

        }
    }

    public void ViewTask(ArrayList<Task> taskList, int buttonId)
    {
        int passedId = taskList.get(buttonId).getId();
        Intent intent = new Intent(getApplicationContext(),ViewTask.class);
        intent.putExtra("Task", passedId);
        startActivity(intent);
    }

}