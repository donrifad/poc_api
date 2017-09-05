import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qa.utils.Log;
import com.qa.utils.ReportBuilder;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by Rifad on 8/28/17.
 */
public class TestBase implements ITestListener {

    public static <R> Object getResponseAsObject(String response, Class<R> classType) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(response, classType);
        } catch (JsonProcessingException e) {
            Log.logException(e);
            return null;
        } catch (IOException e) {
            Log.logException(e);
            return null;
        }
    }

    protected String url = "";
    protected String requestMethod = "";
    StringEntity stringEntity = null;
    protected Map<String, String> headers = null;
    protected String jsonInString = null;
    protected String token = "";
    protected String user_id = "";
    protected int userId = 0;
    protected int featureId = 0;
    protected ObjectMapper mapper = new ObjectMapper();
    static int testCount = 0;
    static ITestNGMethod[] iTestNGMethods;
    static JsonArray elements;
    static JsonArray skippedTests;
    private long testSuiteStarted = 0;
    private long testSuiteDuration = 0;
    private int skipCount = 0;
    protected String body = "";
    protected static StackTraceElement[] cause;
    private static String testClassName = "";
    protected SoftAssert softAssert;


    @BeforeClass
    public void initBaseClass(ITestContext iTestContext) {
        cause = null;
        System.out.println("Running Test '" + this.getClass().getName() + "'");
        skipCount = 0;
        iTestNGMethods = iTestContext.getAllTestMethods();
        testCount = iTestNGMethods.length;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        testSuiteDuration = 0;
        testSuiteStarted = new Date().getTime();
        elements = new JsonArray();
        skippedTests = new JsonArray();
//        ApiCommonRequest.createMerchant();
//        ApiCommonRequest.getAccountId();
//        ApiCommonRequest.getGlobalUserID();

    }

    @AfterClass
    public void cleanUpBaseClass(ITestContext iTestContext) {
        try {
            testSuiteDuration = new Date().getTime() - testSuiteStarted;
            ReportBuilder.generateJsonFile(elements, iTestContext.getAttribute("feature").toString(), testSuiteDuration, testClassName);
            ReportBuilder.updateModules();
        } catch (Exception e) {
            return;
        }
        /*try {
            com.cake.payment.qa.finance.functions.Login.quitDriver();

        }catch (Exception e){}
        try {
            com.cake.payment.qa.adminconsole.functions.Login.quitDriver();

        }catch (Exception e){}*/
    }

    public void onTestStart(ITestResult iTestResult) {

    }

    public void onTestSuccess(ITestResult iTestResult) {
        testClassName = iTestResult.getTestClass().getName();

        elements.add(ReportBuilder.getElement(iTestResult, null));

    }

    public void onTestFailure(ITestResult iTestResult) {
        testClassName = iTestResult.getTestClass().getName();

        byte[] screenShotData = new byte[0];
        if (cause != null) {
            Throwable t = new Throwable();
            t.setStackTrace(cause);
            iTestResult.setThrowable(t);
        }
        elements.add(ReportBuilder.getElement(iTestResult, screenShotData));
    }

    public void onTestSkipped(ITestResult iTestResult) {
        testClassName = iTestResult.getTestClass().getName();

        byte[] screenShotData = new byte[0];
        skippedTests.add(ReportBuilder.getElement(iTestResult, screenShotData));
        ReportBuilder.generateJsonFile(skippedTests, iTestResult.getTestContext().getAttribute("feature").toString(), testSuiteDuration, testClassName);
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    public void onStart(ITestContext iTestContext) {
    }

    @BeforeMethod
    public void onStartTest() {
        softAssert = new SoftAssert();
    }

    @AfterTest
    public void onFinish(ITestContext iTestContext) {
        ReportBuilder.updateModules();
    }


    public void updateHeaders(String key, String value, boolean clearBeforeUpdate) {
        if (clearBeforeUpdate)
            headers.clear();
        headers.put(key, value);
    }



   /* public String getAPIToken(String response) {
        TOKEN = APIResponseUtil.getAPIToken(response);
        return TOKEN;
    }

    public  String getUserId(String response) {
        user_id = APIResponseUtil.getUserId(response);
        return user_id;
    }*/

    public String getFeatureId(String response) {
        try {
            JSONObject responseBody = new JSONObject(response);
            user_id = responseBody.getJSONObject("data").getString("feature_id");
        } catch (JSONException e) {
//            e.printStackTrace();
        } finally {
            return user_id;
        }
    }

    byte[] concatenateByteArrays(byte[] a, byte[] b) {
        int aLength = 0;
        int bLength = 0;
        if (a != null)
            aLength = a.length;
        if (b != null)
            bLength = b.length;
        byte[] result = new byte[aLength + bLength];
        if (a != null)
            System.arraycopy(a, 0, result, 0, aLength);
        if (b != null)
            System.arraycopy(b, 0, result, aLength, bLength);
        if (result.length > 0)
            return result;
        else
            return null;
    }

    public static String getValueFromResponse(String response, String key) {

        JsonParser parser = new JsonParser();
        JsonElement obj2 = parser.parse(response);
        JsonObject jsonObject2 = obj2.getAsJsonObject();
        String value = jsonObject2.get(key).toString();
        return value.replace("\"", "");

    }

}
