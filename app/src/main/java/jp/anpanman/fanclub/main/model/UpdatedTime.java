package jp.anpanman.fanclub.main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by linhphan on 8/22/16.
 */
public class UpdatedTime extends BaseModel {
    @SerializedName("new")
    private UpdatedTimeModel news;

    @SerializedName("otoku")
    private UpdatedTimeModel coupon;

    @SerializedName("present")
    private UpdatedTimeModel present;

    @SerializedName("info")
    private UpdatedTimeModel info ;

    public UpdatedTimeModel getNews() {
        return news;
    }

    public void setNews(UpdatedTimeModel news) {
        this.news = news;
    }

    public UpdatedTimeModel getCoupon() {
        return coupon;
    }

    public void setCoupon(UpdatedTimeModel coupon) {
        this.coupon = coupon;
    }

    public UpdatedTimeModel getPresent() {
        return present;
    }

    public void setPresent(UpdatedTimeModel present) {
        this.present = present;
    }

    public UpdatedTimeModel getInfo() {
        return info;
    }

    public void setInfo(UpdatedTimeModel info) {
        this.info = info;
    }

    public static class UpdatedTimeModel{
        @SerializedName("updatetime")
        private String updatedTime;

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }
    }
}
