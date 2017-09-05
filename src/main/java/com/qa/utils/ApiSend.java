package com.qa.utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.qa.common.Constant;

import java.io.File;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.EncoderConfig.encoderConfig;

/**
 * Created by Rifad on 8/28/17.
 */
public class ApiSend {

    public static Response send(Map<String, String> headers, String bodyString, String uri, String requestMethod) {
        RestAssured.baseURI = Constant.API_HOST;
        RestAssured.basePath = Constant.BASE_PATH;
        RestAssured.port = Constant.PORT;
        RequestSpecification requestSpecification = getRequestSpec(headers, bodyString);
        requestSpecification = given().spec(requestSpecification);
        Response response = execute(requestMethod, requestSpecification, uri);
        //Constant.setResponseCode(ResponseUtil.getResponseCode(requestMethod,requestSpecification,uri));
        return response;
    }

    public static Response sendAuth(Map<String, String> headers, String bodyString, String requestMethod) {
        RestAssured.baseURI = Constant.API_HOST;
        RestAssured.basePath = Constant.AUTH_PATH;
        RestAssured.port = Constant.PORT;
        RequestSpecification requestSpecification = getRequestSpec(headers, bodyString);
        requestSpecification = given().spec(requestSpecification);
        Response response = execute(requestMethod, requestSpecification, "");
        //Constant.setResponseCode(ResponseUtil.getResponseCode(requestMethod,requestSpecification,uri));
        return response;
    }

    public static Response execute(String reqMethod, RequestSpecification requestSpec, String uri) {
        RequestSpecification requestSpecification = requestSpec;
        requestSpecification = given(requestSpecification).config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));

        Response response = null;
        if ("GET".equalsIgnoreCase(reqMethod)) {
            response = requestSpecification.expect().when().get(uri, new Object[0]);
        }
        if ("POST".equalsIgnoreCase(reqMethod)) {
            response = requestSpecification.expect().when().post(uri, new Object[0]);
        }
        if ("PUT".equalsIgnoreCase(reqMethod)) {
            response = requestSpecification.expect().when().put(uri, new Object[0]);
        }
        if ("DELETE".equalsIgnoreCase(reqMethod)) {
            response = requestSpecification.expect().when().delete(uri, new Object[0]);
        }
        if ("PATCH".equalsIgnoreCase(reqMethod)) {
            response = requestSpecification.expect().when().delete(uri, new Object[0]);
        }
        if (response != null)
            return response;
        else
            return null;
    }

    public static RequestSpecification getRequestSpec(Map<String, String> headers, File body) {
        RequestSpecBuilder reqSpecBuilder = new RequestSpecBuilder();
        if (headers != null)
            reqSpecBuilder.addHeaders(headers);
        if (body != null && body.length() > 0) {
            reqSpecBuilder.setBody(body);
        }
        return reqSpecBuilder.build();
    }

    public static RequestSpecification getRequestSpec(Map<String, String> headers, String body) {
        RequestSpecBuilder reqSpecBuilder = new RequestSpecBuilder();
        if (headers != null)
            reqSpecBuilder.addHeaders(headers);
        if (body != null && body.length() > 0) {
            reqSpecBuilder.setBody(body);
        }
        return reqSpecBuilder.build();
    }
}
