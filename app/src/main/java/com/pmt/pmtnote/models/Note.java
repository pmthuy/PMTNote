package com.pmt.pmtnote.models;

import java.io.Serializable;

/**
 * Created by thuypm on 2/23/2016.
 */
public class Note implements Serializable {
    public long id;
    public String text;
    public String dueDate;
    public int priority;

    public Note(){

    }
    public Note(long id, String text, String dueDate, int priority){
        this.id = id;
        this.text = text;
        this.dueDate = dueDate;
        this.priority = priority;
    }
    public Note(String text, String dueDate, int priority){
        this.id = -1;
        this.text = text;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public  String toString(){
        return "ID :" + id +
                " text: " + text +
                " dueDate: " + dueDate +
                " priority: " + priority;

    }
}
