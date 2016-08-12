package jp.anpanman.fanclub.main.parser;

import android.content.Context;

import jp.anpanman.fanclub.framework.restfulService.parser.IParser;

/**
 * Created by chientruong on 8/11/16.
 */
public class DeviceTokenParser implements IParser {
    @Override
    public Object parse(Context context, String data) {
        return data;
    }
}
