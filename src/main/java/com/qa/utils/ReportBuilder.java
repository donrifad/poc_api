package com.qa.utils;

import com.google.common.base.CaseFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qa.common.Constant;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.ITestResult;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;


public class ReportBuilder {
    public static JsonObject getElement(ITestResult iTestResult, byte[] screenShotData) {
        boolean isScreenshotAvailable = !(screenShotData == null || screenShotData.length == 0);
        JsonObject element = new JsonObject();
        JsonArray steps = new JsonArray();
        JsonObject step = new JsonObject();
        JsonObject result = new JsonObject();
        String testDescription = "";
        String testLinkId = "";

        if (iTestResult.getMethod().getDescription() == null) {
            testDescription = iTestResult.getName();
        } else {
            testDescription = iTestResult.getName() + " " + iTestResult.getMethod().getDescription();
        }
        testDescription = testDescription.replace(":", "  :  ");
        element.addProperty("name", testDescription);
        element.addProperty("id", iTestResult.getMethod().getDescription());
        element.addProperty("type", "scenario");
        element.addProperty("keyword", "Test case");
        result.addProperty("duration", (iTestResult.getEndMillis() - iTestResult.getStartMillis()) * 1000 * 1000);
        if (iTestResult.getStatus() == 1) {
            result.addProperty("status", "passed");
        } else if (iTestResult.getStatus() == 2) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            iTestResult.getThrowable().printStackTrace(pw);
            result.addProperty("error_message", sw.toString());
            result.addProperty("status", "failed");
            if (isScreenshotAvailable) {
                JsonArray embeddings = new JsonArray();
                JsonObject embed = new JsonObject();
                sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

                embed.addProperty("data", encoder.encode(screenShotData));
                embed.addProperty("mime_type", "image/png");
                embeddings.add(embed);
                step.add("embeddings", embeddings);
            }
        } else if (iTestResult.getStatus() == 3) {

            result.addProperty("error_message", "Skipped");
            result.addProperty("status", "failed");
            if (isScreenshotAvailable) {
                JsonArray embeddings = new JsonArray();
                JsonObject embed = new JsonObject();
                sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

                embed.addProperty("data", encoder.encode(screenShotData));
                embed.addProperty("mime_type", "image/png");
                embeddings.add(embed);
                step.add("embeddings", embeddings);
            }
        }
        step.addProperty("name", "             ");
        step.addProperty("keyword", "Duration");
        step.add("result", result);
        steps.add(step);
        element.add("steps", steps);
        return element;
    }

    public static void generateJsonFile(JsonArray elements, String featureName, long duration, String className) {
        JsonObject jsonReport = new JsonObject();


        jsonReport.add("elements", elements);
        jsonReport.addProperty("name", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, featureName));
        jsonReport.addProperty("id", UUID.randomUUID().toString().split("-")[0]);
        jsonReport.addProperty("keyword", "Suite");
        jsonReport.addProperty("description", featureName);
        jsonReport.addProperty("duration", String.valueOf((duration + 5000) * 1000 * 1000));
        jsonReport.addProperty("noOfAgents", "1");
        jsonReport.addProperty("testClassName", className);
        jsonReport.addProperty("node", System.getProperty("jenkins.node", "stag_cl2122"));

        jsonReport.addProperty("env", Constant.TEST_ENV);
        jsonReport.addProperty("release", Constant.TEST_RELEASE);
        jsonReport.addProperty("project", Constant.TEST_PROJECT);

        jsonReport.addProperty("uri", featureName.replace("-", "").replace("  ", " ").replace(" ", "_"));

        try {
            FileWriter file = new FileWriter("./target/" + featureName.replace("-", "").replace("  ", " ").replace(" ", "_") + ".json");
            file.write("[" + jsonReport.toString() + "]");
            file.close();


            System.out.print(jsonReport.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Constant.UPDATE_DASHBOARD) {

            try {

                //New Dashboard
                HttpClient client = new DefaultHttpClient();

                HttpPost post = new HttpPost("http://quality.leapset.com:3000/Automations");

                StringEntity input = new StringEntity(jsonReport.toString());
                input.setContentType("application/json");
                post.setEntity(input);

                HttpResponse response = null;
                response = client.execute(post);
                System.out.println("JSON generation " + response.getStatusLine());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateModules() {
        JsonObject jsonReport = new JsonObject();

        jsonReport.addProperty("env", Constant.TEST_ENV);
        jsonReport.addProperty("release", Constant.TEST_RELEASE);
        jsonReport.addProperty("project", Constant.TEST_PROJECT);

        if (Constant.UPDATE_DASHBOARD) {

            try {
                HttpClient client = new DefaultHttpClient();

                HttpPost post = new HttpPost(System.getProperty("ctaf.api.ur", "http://qa-jenkins.leapset.com:8080/ctafapi/ctaf/updatemodule"));

                StringEntity input = new StringEntity(jsonReport.toString());
                input.setContentType("application/json");
                post.setEntity(input);

                HttpResponse response = null;
                response = client.execute(post);
                System.out.println("Modules are updated" + response.getStatusLine());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initRegression() {
        JsonObject jsonReport = new JsonObject();
        jsonReport.addProperty("env", Constant.TEST_ENV);
        jsonReport.addProperty("release", Constant.TEST_RELEASE);
        jsonReport.addProperty("project", Constant.TEST_PROJECT);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(System.getProperty("ctaf.api.ur", "http://qa-jenkins.leapset.com:8080/ctafapi/ctaf/regressionstart"));
            StringEntity input = new StringEntity(jsonReport.toString());
            input.setContentType("application/json");
            post.setEntity(input);
            HttpResponse response = null;
            response = client.execute(post);
            System.out.println("Dashboard cleand" + response.getStatusLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        updateModules();
    }
}
