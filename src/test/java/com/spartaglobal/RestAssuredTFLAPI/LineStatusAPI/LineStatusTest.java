package com.spartaglobal.RestAssuredTFLAPI.LineStatusAPI;
import static io.restassured.RestAssured.*;
import com.spartaglobal.RestAssuredTFLAPI.Config.BaseConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class LineStatusTest
{

    private static Response lineStatusResponse;
    private static String lineToTest;
    @BeforeClass
    public static void setup(){
    baseURI = "https://api.tfl.gov.uk/";
    basePath = "Line/";
    lineToTest = "northern";
    lineStatusResponse = get(lineToTest + "/Status?" + BaseConfig.app_id +"&"+ BaseConfig.app_key);
    }

    @Test
    public void testResponseCode() {
       lineStatusResponse
               .then()
               .assertThat()
               .statusCode(200);
    }

    @Test
    public void testContentTypeIsJSON(){
        lineStatusResponse
                .then()
                .assertThat()
                .contentType(ContentType.JSON);
    }
    @Test
    public void testDisplayFullOutput(){
        lineStatusResponse
                .prettyPrint();
    }

    @Test
    public void testLineNameInsideResponse(){
        lineStatusResponse
                .then()
                .body("[0].name", IsEqualIgnoringCase.equalToIgnoringCase(lineToTest));
    }

    @Test
    public void testCurrentLineStatus(){
        lineStatusResponse
                .then()
                .body("[0].lineStatuses[0].statusSeverityDescription",IsEqualIgnoringCase.equalToIgnoringCase("Good service"));
    }

    @Test
    public void testCurrentLineStatusReason(){
        lineStatusResponse
                .then()
                .body("[0].lineStatuses[0].reason",equalTo(null));
    }
}
