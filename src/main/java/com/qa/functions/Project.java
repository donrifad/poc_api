package com.qa.functions;

import com.jayway.restassured.response.Response;
import com.qa.types.ContentTypes;
import com.qa.common.Headers;
import com.qa.types.HttpMethod;
import com.qa.types.RequestUri;
import com.qa.utils.ApiSend;

/**
 * Created by Rifad on 8/28/17.
 */
public class Project {


    public static Response getProjects() {
        String body = null;
        Response response = ApiSend.send(Headers.getContentTypeHeader(ContentTypes.APPLICATION_JSON), body, RequestUri.GET_PROJECTS, HttpMethod.GET);
        return response;
    }

}
