package com.marker.fabel.android_client.models;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;

public class User {
    private Long id;

    private String phone;

    public User(){

    }

    public User(JSONObject object) throws ParseException {
        try {
            this.id = object.getLong("id");
            this.phone = object.getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
