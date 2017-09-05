package com.qa.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rifad on 9/5/17.
 */
public class ResponseUtil {

    public static String getAPIToken(String response) {
        String token = "";
        try {
            JSONObject responseBody = new JSONObject(response);
            token = responseBody.getString("data");
        } catch (JSONException e) {
//            e.printStackTrace();
        } finally {
            return token;
        }
    }

}
