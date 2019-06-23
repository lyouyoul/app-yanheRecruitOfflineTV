package com.yanhe.recruit.tv.server.convert;

import com.yanhe.recruit.tv.server.inf.ResConvert;
import com.yanhe.recruit.tv.utils.Mount;

import java.util.Date;

/**
 * @author mx
 */
public class DateConvert implements ResConvert {
    public DateConvert() {
        super();
    }
    @Override
    public Date convert (String value) {
        return new Mount(value, "yyyy-MM-dd'T'HH:mm:ss.SSS").getValue();
    }
}
