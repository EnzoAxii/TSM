package com.taskmanager;

import java.io.Serializable;
import java.util.UUID;

class Task implements Serializable{
    public String title;
    public String desc;
    public int priority;
    public String dueDate;
    public TaskStatus status;
    public String id;

    //Constructor
    public Task(String title, String desc, int priority, String dueDate, TaskStatus status){
        this.title = title;
        this.desc = desc;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = status;
        //generates a unique id
        this.id = UUID.randomUUID().toString();
    }
}