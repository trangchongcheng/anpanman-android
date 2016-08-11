package com.main.parser;

import android.content.Context;

import com.framework.restfulService.parser.IParser;

/**
 * Created by chientruong on 8/11/16.
 */
public class DeviceTokenParser implements IParser {
    @Override
    public Object parse(Context context, String data) {
        return data;
    }
}
