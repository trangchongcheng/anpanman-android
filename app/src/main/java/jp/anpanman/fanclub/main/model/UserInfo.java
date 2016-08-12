package jp.anpanman.fanclub.main.model;

import com.google.gson.annotations.SerializedName;

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

    @SerializedName("deviceInfo")
    private String deviceInfo;

    @SerializedName("createDate")
    private String createdDate;

    @SerializedName("badges")
    private Map<String, String> badges;

    public UserInfo() {
    }

    public UserInfo(String objectId, String id, String nickName, Map<String, String> badges) {
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

    public Map<String, String> getBadges() {
        return badges;
    }

    public void setBadges(Map<String, String> badges) {
        this.badges = badges;
    }
}
