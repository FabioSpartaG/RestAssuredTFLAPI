package com.spartaglobal.RestAssuredTFLAPI.ArrivalsByStop;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import com.spartaglobal.RestAssuredTFLAPI.Config.ArrivalsByStopConfig;
import com.spartaglobal.RestAssuredTFLAPI.Config.BaseConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class ArrivalsByStopTest {
    private static Response arrivalResponse;
    @BeforeClass
    public static void setup(){
        baseURI = "https://api.tfl.gov.uk/";
        basePath = "Line/";
        arrivalResponse = get(ArrivalsByStopConfig.line
                + "/Arrivals/"
                + ArrivalsByStopConfig.stopId
                + "?direction="
                + ArrivalsByStopConfig.direction
                +"&app_id="
                + BaseConfig.app_id
                +"&app_key="
                + BaseConfig.app_key
        );
    }

    @Test
    public void testResponseStatusCode(){
        arrivalResponse
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testResponseIsInJSON(){
        arrivalResponse
                .then()
                .assertThat()
                .contentType(ContentType.JSON);
    }

    @Test
    public void displayAllOutput(){
        arrivalResponse
                .prettyPrint();
    }

    /**
     * For this particular scenario, Stockwell is used as a test station
     */
    @Test
    public void testStationNameIsCorrect(){
        arrivalResponse
                .then()
                .body("[0].stationName",equalTo("Stockwell Underground Station"));
    }

    @Test
    public void testDestinationNameIsCorrect() {
        arrivalResponse
                .then()
                .body("[0].destinationName", equalTo("Stockwell Underground Station"));
    }


    /**
     * A method that gets the current location of a train and the distance in seconds from the station
     */
    @Test
    public void sortArrivalTimes() {
        int sizeOfElements = arrivalResponse.jsonPath().getList("$").size();
        Map<String,Integer> locationWithTimeToStationMap = new HashMap<>();
        for(int i = 0; i < sizeOfElements; i++){
            locationWithTimeToStationMap.put(arrivalResponse.jsonPath().getString("currentLocation"+"["+i+"]"),
                   Integer.parseInt(arrivalResponse.jsonPath().getString("timeToStation"+"["+i+"]")));
        }
        locationWithTimeToStationMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
    }
}
