package com.shf.spring.kube.grant;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Description:
 * client-id mode
 *
 * @Author: songhaifeng
 * @Date: 2019/7/7 17:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OAuth2ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ClientCredentialTest {

    @Value("${local.server.port}")
    private int port;

    /**
     * access jetToken with clientId
     *
     * @throws JsonParseException   e
     * @throws JsonMappingException e
     * @throws IOException          e
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void getJwtTokenByTrustedClient() throws JsonParseException, JsonMappingException, IOException, JSONException {
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:" + port + "/oauth/token?client_id=trusted-app&grant_type=client_credentials", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // extract infos
        String responseText = response.getBody();
        HashMap jwtMap = new ObjectMapper().readValue(responseText, HashMap.class);
        assertEquals("bearer", jwtMap.get("token_type"));
        assertEquals("webClient appClient", jwtMap.get("scope"));
        assertTrue(jwtMap.containsKey("access_token"));
        assertTrue(jwtMap.containsKey("expires_in"));
        assertTrue(jwtMap.containsKey("jti"));

        // decode accessToken, extract clientInfo
        String accessToken = (String) jwtMap.get("access_token");
        Jwt jwtToken = JwtHelper.decode(accessToken);
        String claims = jwtToken.getClaims();
        log.info("\n" + new JSONObject(claims).toString(3));

        HashMap claimsMap = new ObjectMapper().readValue(claims, HashMap.class);
        assertEquals("song", ((List<String>) claimsMap.get("aud")).get(0));
        assertEquals("trusted-app", claimsMap.get("client_id"));
        assertEquals("webClient", ((List<String>) claimsMap.get("scope")).get(0));
        assertEquals("appClient", ((List<String>) claimsMap.get("scope")).get(1));
        List<String> authorities = (List<String>) claimsMap.get("authorities");
        assertEquals(1, authorities.size());
        assertEquals("reader", authorities.get(0));
    }

    /**
     * access jetToken with unknown clientId
     */
    @SuppressWarnings({"rawtypes"})
    @Test
    public void accessWithUnknownClientID() {
        ResponseEntity<String> response = new TestRestTemplate("unknown-app", "secret").postForEntity("http://localhost:" + port + "/oauth/token?client_id=unknown-app&grant_type=client_credentials", null, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
