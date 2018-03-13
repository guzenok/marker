package com.marker.fabel.android_client.models;

import com.marker.fabel.android_client.App;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class Sheet {
    private Long id;

    private String name;

    private User author;

    public List<Mark> marks;

    public Sheet(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User u) {
        this.author = u;
    }

    public Integer getMark() {
        if( this.marks == null) return 0;
        Integer sum = 0;
        Integer n = 0;
        for( Mark m : this.marks) {
            if( m.getValue() == 0 || m.getDeleted() != null ) continue;
            sum += m.getValue();
                    n += 1;
        }
        return (n==0)? 0 : (sum / n);
    }

    public List<Mark> notEmptyMarks(){
        ArrayList<Mark> validMarks = new ArrayList<Mark>();
        for( Mark m : this.marks) {
            if( m.getValue()==0 || m.getDeleted() != null ) continue;
            validMarks.add(m);
        }
        return validMarks;
    }

    public Mark getUsersMark(Long user_id){
        if( this.marks == null) return null;
        for( Mark m : this.marks) {
            if( m.getAuthor().getId() == user_id && m.getDeleted() == null ) return m;
        }
        return null;
    }
}
