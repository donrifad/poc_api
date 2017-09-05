package com.qa.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.json.RequestBase;

import java.util.Arrays;

/**
 * Created by Rifad on 8/28/17.
 */
public class ApiJacksonUtil {
    ApiJacksonUtil() {
    }

    public static String getAsString(RequestBase requestBase) {
        return getAsString(requestBase, false);
    }

    public static final String getAsString(RequestBase body, boolean ignoreNull) {
        ObjectMapper mapper = new ObjectMapper();
        if (ignoreNull)
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            jsonInString = Arrays.toString(e.getStackTrace());
            Log.logException(e);
        }
        return jsonInString;
    }

    public static String getAsArrayString(RequestBase... requestBase) {
        return getAsArrayString(true, requestBase);
    }

    public static String getAsArrayString(boolean ignoreNull, RequestBase... requestBase) {
        ObjectMapper mapper = new ObjectMapper();
        if (ignoreNull)
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(requestBase);
        } catch (JsonProcessingException e) {
            jsonInString = Arrays.toString(e.getStackTrace());
            Log.logException(e);
        }
        return jsonInString;
    }

}
