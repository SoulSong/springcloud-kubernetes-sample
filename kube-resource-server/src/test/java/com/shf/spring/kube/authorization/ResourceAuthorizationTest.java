package com.shf.spring.kube.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shf.spring.kube.OAuth2ResourceServerApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/7 21:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OAuth2ResourceServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ResourceAuthorizationTest {

    @Value("${local.server.port}")
    private int port;

    /**
     * test {@link com.shf.spring.kube.oauth2.configuration.ResourceServerConfiguration.OauthRequestedMatcher}
     */
    @Test
    public void no_need_permission_test() {
        final ResponseEntity<String> response = new RestTemplate().getForEntity("http://localhost:" + port + "/resource/no_need_permission", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("no_need_permission", response.getBody());
    }

    @Test
    public void authority_with_user_test() throws IOException {
        // get token by username and password
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:3000/oauth/token?grant_type=password&username=foo1&password=foo123", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final String accessToken = new ObjectMapper().readValue(response.getBody(), HashMap.class).get("access_token").toString();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        response = new RestTemplate().exchange("http://localhost:" + port + "/resource/authority_with_user", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("foo1", response.getBody());
    }

    /**
     * user[foo2] has no role[Admin], except httpStatus[403]
     *
     * @throws IOException e
     */
    @Test
    public void authority_with_user_test2() throws IOException {
        // get token by username and password
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:3000/oauth/token?grant_type=password&username=foo2&password=foo234", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final String accessToken = new ObjectMapper().readValue(response.getBody(), HashMap.class).get("access_token").toString();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        try {
            response = new RestTemplate().exchange("http://localhost:" + port + "/resource/authority_with_user", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch (HttpClientErrorException e) {
            // 403
            assertEquals(HttpStatus.FORBIDDEN.value(), e.getRawStatusCode());
            assertEquals("access_denied", new ObjectMapper().readValue(e.getResponseBodyAsString(), HashMap.class).get("error"));
        }
    }

    @Test
    public void authority_with_clientId_test() throws IOException {
        // get token by username and password
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:3000/oauth/token?client_id=trusted-app&grant_type=client_credentials", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final String accessToken = new ObjectMapper().readValue(response.getBody(), HashMap.class).get("access_token").toString();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        response = new RestTemplate().exchange("http://localhost:" + port + "/resource/authority_with_clientId", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("trusted-app", response.getBody());
    }

    @Test
    public void authority_with_clientId2_test() throws IOException {
        // get token by username and password
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:3000/oauth/token?client_id=trusted-app&grant_type=client_credentials", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final String accessToken = new ObjectMapper().readValue(response.getBody(), HashMap.class).get("access_token").toString();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        try {
            new RestTemplate().exchange("http://localhost:" + port + "/resource/authority_with_clientId2", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch (HttpClientErrorException e) {
            // 403
            assertEquals(HttpStatus.FORBIDDEN.value(), e.getRawStatusCode());
            assertEquals("access_denied", new ObjectMapper().readValue(e.getResponseBodyAsString(), HashMap.class).get("error"));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void get_extrainfo_test() throws IOException {
        // get token by username and password
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:3000/oauth/token?grant_type=password&username=foo1&password=foo123", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final String accessToken = new ObjectMapper().readValue(response.getBody(), HashMap.class).get("access_token").toString();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        response = new RestTemplate().exchange("http://localhost:" + port + "/resource/get_extrainfo", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        HashMap claimsMap = new ObjectMapper().readValue(response.getBody(), HashMap.class);
        assertEquals("song", ((List<String>) claimsMap.get("aud")).get(0));
        assertEquals("trusted-app", claimsMap.get("client_id"));
        assertEquals("foo1", claimsMap.get("user_name"));
        assertEquals("webClient", ((List<String>) claimsMap.get("scope")).get(0));
        assertEquals("appClient", ((List<String>) claimsMap.get("scope")).get(1));
        assertEquals("foo-org", claimsMap.get("organization"));
        assertTrue(((List<String>) claimsMap.get("authorities")).containsAll(Arrays.asList("User", "Admin")));
    }

    @Test
    public void is_expired_test() throws IOException {
        // get token by username and password
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:3000/oauth/token?grant_type=password&username=foo1&password=foo123", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final String accessToken = new ObjectMapper().readValue(response.getBody(), HashMap.class).get("access_token").toString();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        response = new RestTemplate().exchange("http://localhost:" + port + "/resource/is_expired", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("false", response.getBody());
    }

}
