package jp.anpanman.fanclub.main.model;

public enum MainTabs {
    News, Coupon, Present, MyPage, Setting, MainTabs;

    public static MainTabs get(int ordinal) {
        MainTabs tab = News;
        switch (ordinal) {
            case 0:
                tab = News;
                break;
            case 1:
                tab = Coupon;
                break;
            case 2:
                tab = Present;
                break;
            case 3:
                tab = MyPage;
                break;
            case 4:
                tab = Setting;
                break;
        }

        return tab;
    }
}
