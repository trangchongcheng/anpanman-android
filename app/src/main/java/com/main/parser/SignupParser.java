package com.main.parser;

import android.content.Context;

import com.framework.restfulService.parser.IParser;
import com.main.model.UserInfo;

/**
 * Created by linhphan on 8/5/16.
 */
public class SignupParser implements IParser {
    @Override
    public Object parse(Context context, String data) {
        //{"id":"6652297908","nickname":"","badges":[],"deviceInfo":"jrlBBy5d2qVEpzzl","createDate":"2016-08-05T04:42:27.568Z","objectId":"9fARv4E5yIInmLg8"}
        return UserInfo.fromJson(data, UserInfo.class);
    }
}
