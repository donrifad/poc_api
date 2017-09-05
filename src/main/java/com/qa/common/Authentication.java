package com.qa.common;

import com.jayway.restassured.response.Response;
import com.qa.types.AcceptTypes;
import com.qa.types.ContentTypes;
import com.qa.types.HttpMethod;
import com.qa.utils.ApiSend;

import java.util.HashMap;

/**
 * Created by Rifad on 8/28/17.
 */
public class Authentication {
    private static String token = "";

    public static String getAuthToken(boolean haveToken) {

        if (!haveToken) {
            return "";
        }
        if (!token.equals(""))
            return token;
        else {
            setAuthToken();
            return token;
        }
    }

    public static void setAuthToken() {
        String body = "";
        Response response = ApiSend.sendAuth(getBasicAuthHeaders(), body, HttpMethod.POST);
        token = "Bearer " + response.asString().split("\"access_token\":\"")[1].split("\",\"")[0];
    }

    private static HashMap<String, String> getBasicAuthHeaders() {
        HashMap<String, String> basicHeaders = Headers.getBearerAuthorizationTypeHeader(ContentTypes.APPLICATION_FORM_URL, AcceptTypes.APPLICATION_JSON);
        basicHeaders.put("Authorization", "");
        return basicHeaders;
    }
}