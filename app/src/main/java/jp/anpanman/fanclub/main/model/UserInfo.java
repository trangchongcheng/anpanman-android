package jp.anpanman.fanclub.main.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by linhphan on 8/5/16.
 */
public class UserInfo extends BaseModel{
    @SerializedName("objectId")
    private String objectId;

    @SerializedName("id")
    private String id;

    @SerializedName("nickname")
    private String nickName;


    @SerializedName("favorite_character_code")
    private String favorite_character_code;

    @SerializedName("createDate")
    private String createdDate;

    @SerializedName("badges")
    private ArrayList<Badges> badges;
//    private LinkedHashMap<String, String> badges;//<id, name>

    public UserInfo() {
    }

//    public UserInfo(String objectId, String id, String nickName, LinkedHashMap<String, String> badges) {
//        this.objectId = objectId;
//        this.id = id;
//        this.nickName = nickName;
//        this.badges = badges;
//    }
  public UserInfo(String objectId, String id, String nickName, ArrayList<Badges> badges) {
        this.objectId = objectId;
        this.id = id;
        this.nickName = nickName;
        this.badges = badges;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFavorite_character_code() {
        return favorite_character_code;
    }

    public void setFavorite_character_code(String favorite_character_code) {
        this.favorite_character_code = favorite_character_code;
    }


    //    public Map<String, String> getBadges() {
//        return badges;
//    }
//
//    public void setBadges(LinkedHashMap<String, String> badges) {
//        this.badges = badges;
//    }
    public ArrayList<Badges>  getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<Badges> badges) {
        this.badges = badges;
    }
}
