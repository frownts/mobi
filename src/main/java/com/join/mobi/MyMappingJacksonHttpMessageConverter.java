package com.join.mobi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-22
 * Time: 下午10:43
 */
public class MyMappingJacksonHttpMessageConverter extends MappingJacksonHttpMessageConverter {
    public MyMappingJacksonHttpMessageConverter(){
        super();

        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        setSupportedMediaTypes(supportedMediaTypes);
        getObjectMapper().disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

    }
}
