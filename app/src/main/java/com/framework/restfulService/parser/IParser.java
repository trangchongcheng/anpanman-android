package com.framework.restfulService.parser;

import android.content.Context;

import com.framework.restfulService.RestfulService;

/**
 * Created by linhphan on 11/12/15.
 */
public interface IParser {
    Object parse(Context context, Object data);
}
