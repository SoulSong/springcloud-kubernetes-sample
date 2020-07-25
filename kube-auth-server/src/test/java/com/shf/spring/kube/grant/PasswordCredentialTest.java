package com.shf.spring.kube.grant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shf.spring.kube.OAuth2ServerApplication;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Description:
 * password mode
 *
 * @Author: songhaifeng
 * @Date: 2019/7/7 17:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OAuth2ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class PasswordCredentialTest {

    @Value("${local.server.port}")
    private int port;

    /**
     * access jetToken with password
     *
     * @throws IOException e
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void getJwtToken() throws IOException, JSONException {
        // get token by username and password
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:" + port + "/oauth/token?grant_type=password&username=foo1&password=foo123", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String responseText = response.getBody();

        // extract infos
        HashMap jwtMap = new ObjectMapper().readValue(responseText, HashMap.class);
        assertEquals("bearer", jwtMap.get("token_type"));
        assertEquals("webClient appClient", jwtMap.get("scope"));
        assertTrue(jwtMap.containsKey("access_token"));
        assertTrue(jwtMap.containsKey("refresh_token"));
        assertTrue(jwtMap.containsKey("expires_in"));
        assertTrue(jwtMap.containsKey("jti"));

        // decode accessToken, extract userInfo
        String accessToken = (String) jwtMap.get("access_token");
        Jwt jwtToken = JwtHelper.decode(accessToken);
        String claims = jwtToken.getClaims();
        log.info("\n" + new JSONObject(claims).toString(3));

        HashMap claimsMap = new ObjectMapper().readValue(claims, HashMap.class);
        assertEquals("song", ((List<String>) claimsMap.get("aud")).get(0));
        assertEquals("trusted-app", claimsMap.get("client_id"));
        assertEquals("foo1", claimsMap.get("user_name"));
        assertEquals("webClient", ((List<String>) claimsMap.get("scope")).get(0));
        assertEquals("appClient", ((List<String>) claimsMap.get("scope")).get(1));
        assertEquals("foo-org", claimsMap.get("organization"));
        assertTrue(((List<String>) claimsMap.get("authorities")).containsAll(Arrays.asList("User", "Admin")));
    }

    /**
     * check accessToken
     *
     * @throws IOException e
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void checkToken() throws IOException {
        // get accessToken by username and password
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret")
                .postForEntity("http://localhost:" + port + "/oauth/token?grant_type=password&username=foo1&password=foo123", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //checkToken
        response = new RestTemplate().postForEntity("http://localhost:" + port + "/oauth/check_token?token=" +
                new ObjectMapper().readValue(response.getBody(), HashMap.class).get("access_token"), null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        HashMap results = new ObjectMapper().readValue(response.getBody(), HashMap.class);
        assertEquals("song", ((List<String>) results.get("aud")).get(0));
        assertEquals("trusted-app", results.get("client_id"));
        assertEquals("foo1", results.get("user_name"));
        assertTrue(Boolean.valueOf(results.get("active").toString()));
        assertEquals("webClient", ((List<String>) results.get("scope")).get(0));
        assertEquals("appClient", ((List<String>) results.get("scope")).get(1));
        assertEquals("foo-org", results.get("organization"));
        assertTrue(((List<String>) results.get("authorities")).containsAll(Arrays.asList("User", "Admin")));
    }

    /**
     * refreshToken
     *
     * @throws IOException   e
     * @throws JSONException e
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void refreshToken() throws IOException, JSONException {
        // get accessToken by username and password
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret")
                .postForEntity("http://localhost:" + port + "/oauth/token?grant_type=password&username=bar1&password=bar123", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //checkToken
        response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:" + port + "/oauth/token?grant_type=refresh_token&refresh_token=" +
                new ObjectMapper().readValue(response.getBody(), HashMap.class).get("refresh_token"), null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // extract infos
        HashMap jwtMap = new ObjectMapper().readValue(response.getBody(), HashMap.class);
        assertEquals("bearer", jwtMap.get("token_type"));
        assertEquals("webClient appClient", jwtMap.get("scope"));
        assertTrue(jwtMap.containsKey("access_token"));
        assertTrue(jwtMap.containsKey("refresh_token"));
        assertTrue(jwtMap.containsKey("expires_in"));
        assertTrue(jwtMap.containsKey("jti"));

        // decode accessToken, extract userInfo
        String accessToken = (String) jwtMap.get("access_token");
        Jwt jwtToken = JwtHelper.decode(accessToken);
        String claims = jwtToken.getClaims();
        log.info("\n" + new JSONObject(claims).toString(3));

        HashMap claimsMap = new ObjectMapper().readValue(claims, HashMap.class);
        assertEquals("song", ((List<String>) claimsMap.get("aud")).get(0));
        assertEquals("trusted-app", claimsMap.get("client_id"));
        assertEquals("bar1", claimsMap.get("user_name"));
        assertEquals("webClient", ((List<String>) claimsMap.get("scope")).get(0));
        assertEquals("appClient", ((List<String>) claimsMap.get("scope")).get(1));
        assertEquals("bar-org", claimsMap.get("organization"));
        assertTrue(((List<String>) claimsMap.get("authorities")).containsAll(Arrays.asList("Guest", "Admin")));
    }

}
