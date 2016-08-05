package com.main.model;

import com.google.gson.Gson;

/**
 * Created by linhphan on 8/5/16.
 */
public class BaseModel {

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static <T extends BaseModel>T fromJson(String json, Class<T> T){
        Gson gson = new Gson();
        return gson.fromJson(json, T);
    }

    @Override
    public String toString() {
        return toJson();
    }
}
