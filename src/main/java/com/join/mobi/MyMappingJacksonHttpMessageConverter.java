package com.join.mobi;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import java.util.Collections;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-22
 * Time: 下午10:43
 */
public class MyMappingJacksonHttpMessageConverter extends MappingJacksonHttpMessageConverter {
    public MyMappingJacksonHttpMessageConverter(){
        super();
        setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
    }
}
