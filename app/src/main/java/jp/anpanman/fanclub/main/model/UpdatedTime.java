package jp.anpanman.fanclub.main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by linhphan on 8/22/16.
 */
public class UpdatedTime extends BaseModel {
    @SerializedName("newarrival")
    private String news;

    @SerializedName("profit")
    private String coupon;

    @SerializedName("present")
    private String present;

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }
}
