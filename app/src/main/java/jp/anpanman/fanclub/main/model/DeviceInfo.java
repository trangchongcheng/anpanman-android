package jp.anpanman.fanclub.main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chientruong on 9/21/16.
 */
public class DeviceInfo  extends BaseModel{
    @SerializedName("__type")
    private String __type;
    @SerializedName("className")
    private String className;
    @SerializedName("objectId")
    private String objectId;

    public DeviceInfo(String __type, String className, String objectId) {
        this.__type = __type;
        this.className = className;
        this.objectId = objectId;
    }

    public DeviceInfo() {
    }

    public String get__type() {
        return __type;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
