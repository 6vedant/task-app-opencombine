package com.example.swiftandroidexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swiftandroidexample.adapter.RecyclerViewAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // initializing swift runtime.
            // The first argument is a pointer to java context (activity in this case).
            // The second argument should always be false.
            org.swift.swiftfoundation.SwiftFoundation.Initialize(this, false);
        } catch (Exception err) {
            android.util.Log.e("SwiftAndroidExample", "Can't initialize swift foundation: " + err.toString());
        }

        // loading dynamic library containing swift code
        System.loadLibrary("SwiftAndroidExample");

        initUIWidgets();

        initTaskManager();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a task
                takeTaskInput();
            }
        });
    }

    private void takeTaskInput() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a Task");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskText = input.getText().toString();
                addTask(taskText);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void initUIWidgets() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    // method is called from swift code, and result is sent as input parameter
    public void printTasks(String taskResultString) {
        if (taskResultString.isEmpty()) {
            // show add tasks
            findViewById(R.id.noTasksPresentLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutTasks).setVisibility(View.GONE);
            return;
        }
        findViewById(R.id.noTasksPresentLayout).setVisibility(View.GONE);
        findViewById(R.id.layoutTasks).setVisibility(View.VISIBLE);
        List<String> tasks = getTasksFromString(taskResultString);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, tasks, new RecyclerViewAdapter.OnTaskItemClickListner() {
            @Override
            public void onTaskItemClick(String taskModel) {
                showDialogToRemoveTask(taskModel);
            }

            @Override
            public boolean onTaskItemLongClick(String taskModel) {
                return false;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void showDialogToRemoveTask(String taskModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove the task: " + taskModel);

        // Set up the buttons
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeTask(taskModel);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private List<String> getTasksFromString(String taskResultString) {
        ArrayList<String> taskList = new ArrayList<>();
        taskList.clear();
        String[] strArr = taskResultString.split("#@");
        return Arrays.asList(strArr);
    }

    public native void addTask(String taskName);

    public native void removeTask(String taskName);

    // custom method to be called from java
    // implementation of method is in Swift file
    private native void initTaskManager();


}