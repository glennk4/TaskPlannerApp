package com.glendall.taskplannerapp;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.RequiresApi;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ViewTask extends AppCompatActivity
    {

        DatabaseManager db ;
        int taskId = 0;
        Button completeBtn;
        Button deleteBtn;
        Button editBtn;
        String oldDate;

        Task selectedTask = new Task();


        @SuppressLint("Range")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onCreate(Bundle savedInstanceState) 
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.viewtasklayout);
            Intent intent = getIntent();
            taskId = intent.getIntExtra("Task",0);

            db= new DatabaseManager(this);
            Cursor result = db.GetTaskData(taskId);
            completeBtn = findViewById(R.id.completeBtn);
            completeBtn = findViewById(R.id.completeBtn);
            completeBtn.setOnClickListener(
                    v -> CompleteTask(taskId)
            );
            deleteBtn = findViewById(R.id.deleteBtn);
            deleteBtn.setOnClickListener(
                    v -> DeleteTask(taskId)
            );

            selectedTask = new Task();
            TextView taskName = findViewById(R.id.taskTitle);
            TextView taskDesc =  findViewById(R.id.descBox);
            TextView dueDate = findViewById(R.id.taskDue);

            editBtn = findViewById(R.id.editBtn);
            editBtn = findViewById(R.id.editBtn);
            editBtn.setOnClickListener(
                    v -> Log.d("EDIT BUTTON", "Code edit here")
            );

            if (result.getCount() == 0) 
            {
                Toast.makeText(getApplicationContext(), "No Data to show", Toast.LENGTH_SHORT).show();
            }
            else
            {
                while (result.moveToNext())
                {
                    selectedTask.id = result.getInt(result.getColumnIndex("ID"));
                    selectedTask.taskName = result.getString(result.getColumnIndex("NAME"));
                    selectedTask.taskDescription = result.getString(result.getColumnIndex("DESCRIPTION"));
                    selectedTask.dueDate = result.getString(result.getColumnIndex("DUE"));
                    selectedTask.completed = result.getInt((result.getColumnIndex("COMPLETED")));
                    selectedTask.doneDate = result.getString(result.getColumnIndex("DONE_DATE"));
                }
            }
            taskName.setText(selectedTask.taskName);
            taskDesc.setText(selectedTask.getTaskDescription());

//Format of date - mn
            dueDate.setText(DateToCheck(selectedTask.getId(),selectedTask.getDueDate(),selectedTask.getDoneDate(),selectedTask.getCompleted()));
        }

        public void DeleteTask(int taskId)
        {
            db.DeleteTask(taskId);
            Toast.makeText(ViewTask.this, "Task Deleted!",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }

        public void CompleteTask(int taskId)
        {
            db.CompleteTask(taskId);
            Toast.makeText(ViewTask.this, "Task Completed!",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }

        public void EditTask(int taskId, Task selectedTask, EditText taskName, EditText taskDesc,
                             EditText dueDate, String oldDate)
        {
            int editedTask = taskId;;
            String name = taskName.getText().toString();
            String description = taskDesc.getText().toString();
            String due = oldDate;

            if (due.equals(null))
            {
                Toast.makeText(ViewTask.this, "No Date Added",
                        Toast.LENGTH_LONG).show();
            }
            else
            {
                db.EditTask(name, description, due, editedTask);
                Toast.makeText(ViewTask.this, "Task Edited!",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public String dateFormatting(String newDate)
        {
            String formattedDate;
            String year = newDate.substring(0,4);
            String day;
            Log.d("DATE FORMAT",newDate);

            if (newDate.charAt(6) == '-'&&newDate.charAt(8)==' ')
            {
                day= newDate.substring(7,8);
            }
            else if(newDate.charAt(6) == '-'&&newDate.charAt(8)!=' ')
            {
                day= newDate.substring(7,9);
            }
            else if(newDate.charAt(7) == '-'&&newDate.charAt(9)==' ')
            {
                day= newDate.substring(8,9);
            }
            else
            {
                day= newDate.substring(8,10);
            }

            String monthString = null;
            if (newDate.charAt(6) == '-')
            {
                monthString= newDate.substring(5,6);
            }
            if(newDate.charAt(7) == '-'){
                monthString= newDate.substring(5,7);
            }
            int value = Integer.parseInt(monthString);
            Month newMonth = Month.of(value);

            formattedDate = day+" "+newMonth+" "+year;
            return formattedDate;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private String DateToCheck(int taskId, String dueDate, String doneDate, int completed)
        {
            String dueOrDone;

            if(completed ==1 && !doneDate.equals(null))
            {
                dueOrDone = "Task completed on "+dateFormatting(doneDate);
            }
            else if(completed ==1 && doneDate == null)
            {
                dueOrDone = "Task completed.";
            }
            else
            {
                dueOrDone = "Task due by "+dateFormatting(dueDate);
            }

            return dueOrDone;
        }
}
