package jp.anpanman.fanclub.main.model;

import android.content.Context;
import android.text.TextUtils;

import com.main.R;

import jp.anpanman.fanclub.main.util.Constant;

/**
 * Created by chientruong on 8/25/16.
 */
public class UserCharacter extends BaseModel {
    private int id;
    private String name;
    private String iconName;
    private String layout;
    private int iconResource;
    private int bgResource;

    public UserCharacter(int id, String name, int iconResource, int bgResource) {
        this.id = id;
        this.name = name;
        this.iconResource = iconResource;
        this.bgResource = bgResource;
    }

    public UserCharacter(int id, String name, String iconName, String layout) {
        this.id = id;
        this.name = name;
        this.iconName = iconName;
        this.layout = layout;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public int getIconResource() {
        return iconResource;
    }

    public int getBgResource() {
        return bgResource;
    }

    public static UserCharacter getUserCharacter(Context context, String id){
        int intId;
        try {
            intId = Integer.parseInt(id);
        }catch (NumberFormatException e){
            intId = 99;
        }
        return getUserCharacter(context, intId);
    }

    public static UserCharacter getUserCharacter(Context context,int id){
        UserCharacter userCharacter = null;

        if (id < Constant.FAVORITE_CHARACTER_CODE_MIN || id > Constant.FAVORITE_CHARACTER_CODE_MAX){
            id = Constant.FAVORITE_CHARACTER_CODE_DEFAULT;
        }

        if (id == Constant.FAVORITE_CHARACTER_CODE_DEFAULT){
            userCharacter = new UserCharacter(id, context.getResources().getStringArray(R.array.name_character)[0],
                    context.getResources().obtainTypedArray(R.array.iconResource).getResourceId(0, 0),
                    context.getResources().obtainTypedArray(R.array.bgResource).getResourceId(0, 0));
        }else {
            userCharacter = new UserCharacter(id,context.getResources().getStringArray(R.array.name_character)[(id-1)],
                    context.getResources().obtainTypedArray(R.array.iconResource).getResourceId((id - 1), 0),
                    context.getResources().obtainTypedArray(R.array.bgResource).getResourceId(id - 1, 0));
        }

        return userCharacter;
    }

}
