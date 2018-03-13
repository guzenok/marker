package com.marker.fabel.android_client.models;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.util.Date;

public class Mark {
    private Long id;

    private User author;

    private Integer value;

    private String descr;

    private Date dt;

    private Date deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User u) {
        this.author = u;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date dt) {
        this.deleted = dt;
    }

}
