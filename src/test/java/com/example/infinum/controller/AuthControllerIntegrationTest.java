package com.example.infinum.controller;

import com.example.infinum.dto.AccessToken;
import com.example.infinum.dto.UserDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Test
    public void signinUser() throws JSONException {
        JSONObject request = new JSONObject();
        request.put("email", "test01@test.com");
        request.put("password", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<AccessToken> response = restTemplate.exchange(
                createURLWithPort("/api/auth/signin"), HttpMethod.POST, entity, AccessToken.class);

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getStatusCode().value(), 200);
    }

    @Test
    public void signinUserBadRequest() throws JSONException {
        JSONObject request = new JSONObject();
        request.put("passwordd", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<AccessToken> response = restTemplate.exchange(
                createURLWithPort("/api/auth/signin"), HttpMethod.POST, entity, AccessToken.class);

        Assert.assertEquals(response.getStatusCode().value(), 400);
    }

    @Test
    public void signupUser() throws JSONException {
        JSONObject request = new JSONObject();
        request.put("email", "test011@test.com");
        request.put("password", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<UserDto> response = restTemplate.exchange(
                createURLWithPort("/api/auth/signup"), HttpMethod.POST, entity, UserDto.class);

        Assert.assertEquals(response.getBody().getEmail(), "test011@test.com");
        Assert.assertEquals(response.getStatusCode().value(), 200);
    }

    @Test
    public void signupUserBadRequest() throws JSONException {
        JSONObject request = new JSONObject();
        request.put("emailads", "test011@test.com");
        request.put("passwordasd", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<UserDto> response = restTemplate.exchange(
                createURLWithPort("/api/auth/signup"), HttpMethod.POST, entity, UserDto.class);

        Assert.assertEquals(response.getStatusCode().value(), 400);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
