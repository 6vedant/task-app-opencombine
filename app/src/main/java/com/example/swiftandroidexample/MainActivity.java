package com.example.swiftandroidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

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
    }

    private void initUIWidgets() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    }

    // method is called from swift code, and result is sent as input parameter
    public void printTasks(String taskResultString) {
        Log.d("TAG", taskResultString);
    }

    public native void addTask(String task, long taskCreationTime);

    public native void removeTask(String task);

    // custom method to be called from java
    // implementation of method is in Swift file
    private native void initTaskManager();


}