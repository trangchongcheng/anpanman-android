package jp.anpanman.fanclub.main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chientruong on 9/15/16.
 */
public class Badges extends BaseModel{
    @SerializedName("id")
    private String id = null;
    @SerializedName("name")
    private String name = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Badges() {
    }

    public Badges(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
