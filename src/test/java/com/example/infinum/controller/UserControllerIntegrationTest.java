package com.example.infinum.controller;

import com.example.infinum.dto.AccessToken;
import com.example.infinum.dto.UserDto;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();
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
    public void getAllActiveUsers() throws JSONException {
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<UserDto[]> response = restTemplate.exchange(
                createURLWithPort("/api/user/all"), HttpMethod.GET, entity, UserDto[].class);

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().length, 3);
    }

    @Test
    public void getSingleActiveUser() throws JSONException {
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<UserDto> response = restTemplate.exchange(
                createURLWithPort("/api/user/1"), HttpMethod.GET, entity, UserDto.class);

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getEmail(), "test01@test.com");
    }

    @Test
    public void getSingleInactiveUser() throws JSONException {
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<UserDto> response = restTemplate.exchange(
                createURLWithPort("/api/user/2"), HttpMethod.GET, entity, UserDto.class);

        Assert.assertNull(response.getBody().getEmail());
    }

    @Test
    public void addFavouriteCity() throws JSONException {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", String.format("Bearer %s", token));
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<UserDto> response = restTemplate.exchange(
                createURLWithPort("/api/user/city/2"), HttpMethod.POST, entity, UserDto.class);

        Assert.assertEquals(response.getBody().getEmail(), "test01@test.com");
        Assert.assertNotNull(response.getBody().getFavouriteCities());
        Assert.assertEquals(response.getBody().getFavouriteCities().size(), 1);
    }

    @Test
    public void removeFavouriteCity() throws JSONException {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", String.format("Bearer %s", token));
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<UserDto> response = restTemplate.exchange(
                createURLWithPort("/api/user/city/2"), HttpMethod.PUT, entity, UserDto.class);

        Assert.assertEquals(response.getBody().getEmail(), "test01@test.com");
        Assert.assertNotNull(response.getBody().getFavouriteCities());
        Assert.assertEquals(response.getBody().getFavouriteCities().size(), 0);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
