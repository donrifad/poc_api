package com.qa.common;

import com.qa.types.ContentTypes;
import org.apache.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leshana on 2/23/17.
 */
public class Headers {
    Headers() {
    }

    public static Map<String, String> getContentTypeHeader(String contentType) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(HttpHeaders.CONTENT_TYPE, contentType);
        headers.put(HttpHeaders.ACCEPT, ContentTypes.APPLICATION_JSON);
        headers.put(HttpHeaders.AUTHORIZATION, Authentication.getAuthToken(false));
        return headers;
    }


    public static HashMap<String, String> getUsernamepasswordAuthenticationTypeHeader(String contentType, String acceptTypes, String username, String pin, String password) {
        HashMap headers = new HashMap();
        headers.put("Content-Type", contentType);
        headers.put("Accept", acceptTypes);
        headers.put("username", username);
        headers.put("pin", pin);
        headers.put("password", password);
        return headers;
    }

    public static HashMap<String, String> getHeaders(String email, String password) {
        HashMap headers = new HashMap();
        headers.put("Content-type", "application/json");
        headers.put("x-email", email);
        headers.put("x-password", password);
        return headers;
    }


    public static HashMap<String, String> getBearerAuthorizationTypeHeader(String contentType, String acceptTypes) {
        HashMap headers = new HashMap();
        headers.put("Content-Type", contentType);
        headers.put("Accept", acceptTypes);
        headers.put("Authorization", "Bearer " + Constant.TOKEN);
        return headers;
    }

    public static HashMap<String, String> getHeaders(String token) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", ContentTypes.APPLICATION_JSON);
        headers.put("x-token", token);
        return headers;
    }

}
