import com.jayway.restassured.response.Response;
import com.qa.functions.Project;
import com.qa.json.response.ProjectResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.ITestContext;

import java.io.IOException;

/**
 * Created by Rifad on 7/12/17.
 */
@Listeners(TestBase.class)
public class TestApi extends TestBase {

    @BeforeClass
    public void init(ITestContext iTestContext) throws IOException {
        iTestContext.setAttribute("feature", "Test API - Get Projects");


    }

    @Test(description = "Test Case One")
    public void testApi() throws JSONException {

        Response response = Project.getProjects();
        JSONArray JSONResponseBody = new JSONArray(response.asString());
        // actual = ApiJacksonUtil.getAsArrayString(response.asA(), JobResponse.class);
        ProjectResponse actual = (ProjectResponse) getResponseAsObject(JSONResponseBody.getString(1), ProjectResponse.class);

    }

    @Test(description = "Test Case Two", dependsOnMethods = "testApi")
    public void testApiTwo() throws JSONException {

        Response response = Project.getProjects();
        JSONArray JSONResponseBody = new JSONArray(response.asString());
        // actual = ApiJacksonUtil.getAsArrayString(response.asA(), JobResponse.class);
        ProjectResponse actual = (ProjectResponse) getResponseAsObject(JSONResponseBody.getString(1), ProjectResponse.class);

    }


}
