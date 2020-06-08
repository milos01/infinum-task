package com.example.infinum.controller;

import com.example.infinum.dto.AccessToken;
import com.example.infinum.dto.CityDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityControllerIntegrationTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    private String token;

    @Before
    public void signinUser() throws JSONException {
        JSONObject request = new JSONObject();
        request.put("email", "test01@test.com");
        request.put("password", "password");

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        ResponseEntity<AccessToken> response = restTemplate.exchange(
                createURLWithPort("/api/auth/signin"), HttpMethod.POST, entity, AccessToken.class);

        token = response.getBody().getAccessToken();
    }

    @Test
    public void addCity() throws JSONException {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", String.format("Bearer %s", token));

        JSONObject request = new JSONObject();
        request.put("name", "test");
        request.put("description", "desc");
        request.put("population", "20");

        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        ResponseEntity<CityDto> response = restTemplate.exchange(
                createURLWithPort("/api/city"), HttpMethod.POST, entity, CityDto.class);

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getName(), "test");
    }

    @Test
    public void updateCity() throws JSONException {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", String.format("Bearer %s", token));

        JSONObject request = new JSONObject();
        request.put("name", "testUpd");
        request.put("description", "descUpd");
        request.put("population", "30");

        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        ResponseEntity<CityDto> response = restTemplate.exchange(
                createURLWithPort("/api/city/1"), HttpMethod.PUT, entity, CityDto.class);

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getName(), "testUpd");
        Assert.assertEquals(response.getBody().getDescription(), "descUpd");
        Assert.assertEquals(response.getBody().getPopulation(), 30);
    }

    @Test
    public void updateNotExistingCity() throws JSONException {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", String.format("Bearer %s", token));

        JSONObject request = new JSONObject();
        request.put("name", "testUpd");
        request.put("description", "descUpd");
        request.put("population", "30");

        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        ResponseEntity<CityDto> response = restTemplate.exchange(
                createURLWithPort("/api/city/30"), HttpMethod.PUT, entity, CityDto.class);

        Assert.assertEquals(response.getStatusCode().value(), 404);
    }

    @Test
    public void updateDeletedCity() throws JSONException {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", String.format("Bearer %s", token));

        JSONObject request = new JSONObject();
        request.put("name", "testUpd");
        request.put("description", "descUpd");
        request.put("population", "30");

        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        ResponseEntity<CityDto> response = restTemplate.exchange(
                createURLWithPort("/api/city/3"), HttpMethod.PUT, entity, CityDto.class);

        Assert.assertEquals(response.getStatusCode().value(), 404);
    }

    @Test
    @Transactional
    public void deleteCity() throws JSONException {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", String.format("Bearer %s", token));

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<CityDto> response = restTemplate.exchange(
                createURLWithPort("/api/city/1"), HttpMethod.DELETE, entity, CityDto.class);

        Assert.assertEquals(response.getStatusCode().value(), 200);
    }

    @Test
    public void deleteAlreadyDeletedCity() throws JSONException {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", String.format("Bearer %s", token));

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<CityDto> response = restTemplate.exchange(
                createURLWithPort("/api/city/3"), HttpMethod.DELETE, entity, CityDto.class);

        Assert.assertEquals(response.getStatusCode().value(), 404);
    }

    @Test
    public void getSingleCity() throws JSONException {
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<CityDto> response = restTemplate.exchange(
                createURLWithPort("/api/city/2"), HttpMethod.GET, entity, CityDto.class);

        Assert.assertEquals(response.getBody().getName(), "Paris");
        Assert.assertEquals(response.getBody().getDescription(), "desc2");
        Assert.assertEquals(response.getBody().getPopulation(), 200);
    }

    @Test
    public void getSingleDeletedCity() throws JSONException {
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<CityDto> response = restTemplate.exchange(
                createURLWithPort("/api/city/3"), HttpMethod.GET, entity, CityDto.class);

        Assert.assertEquals(response.getStatusCode().value(), 404);
    }

    @Test
    public void getAllCities() throws JSONException {
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<CityDto[]> response = restTemplate.exchange(
                createURLWithPort("/api/city/all"), HttpMethod.GET, entity, CityDto[].class);

        Assert.assertEquals(response.getBody().length, 10);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
