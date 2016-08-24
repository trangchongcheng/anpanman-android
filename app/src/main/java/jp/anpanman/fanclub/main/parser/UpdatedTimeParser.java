package jp.anpanman.fanclub.main.parser;

import android.content.Context;
import android.util.Log;

import jp.anpanman.fanclub.framework.restfulService.parser.IParser;
import jp.anpanman.fanclub.main.model.UpdatedTime;

/**
 * Created by linhphan on 8/22/16.
 */
public class UpdatedTimeParser implements IParser{
    @Override
    public Object parse(Context context, String data) {
        Log.e("**&&**", "here");
        return UpdatedTime.fromJson(data, UpdatedTime.class);
    }
}
