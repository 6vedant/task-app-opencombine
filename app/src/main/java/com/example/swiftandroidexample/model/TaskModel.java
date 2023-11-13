package com.example.swiftandroidexample.model;

import java.io.Serializable;

public class TaskModel implements Serializable {
    private String ID, title;
    private long taskCreationDate;
    private boolean isCompleted;

    public TaskModel(String ID, String title, long taskCreationDate, boolean isCompleted) {
        this.ID = ID;
        this.title = title;
        this.taskCreationDate = taskCreationDate;
        this.isCompleted = isCompleted;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTaskCreationDate() {
        return taskCreationDate;
    }

    public void setTaskCreationDate(long taskCreationDate) {
        this.taskCreationDate = taskCreationDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
