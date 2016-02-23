package com.pmt.pmtnote.models;

import java.io.Serializable;

/**
 * Created by thuypm on 2/23/2016.
 */
public class Note implements Serializable {
    public long id;
    public String text;

    public Note(long id, String text){
        this.id = id;
        this.text = text;
    }
    public Note(String text){
        this.id = -1;
        this.text = text;
    }
}
