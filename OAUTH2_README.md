# introduction
There are two modules:
- kube-auth-server
- kube-resource-server

# How to test
## Authentication 
### get accessToken 
#### password mode
> curl -XPOST "trusted-app:secret@localhost:3000/oauth/token" -d "grant_type=password&username=foo1&password=foo123"

#### clientId mode
> curl -XPOST "trusted-app:secret@localhost:3000/oauth/token" -d "client_id=trusted-app&grant_type=client_credentials"
 
### check token
> curl  "http://localhost:3000/oauth/check_token?token={access_token}"

### refresh token
> curl  -XPOST "trusted-app:secret@localhost:3000/oauth/token" -d "grant_type=refresh_token&refresh_token={refresh_token}

## Authorization
### example
> curl -H "Authorization:Bearer {access_token}" "http://localhost:3001/resource/authority_with_user"

## Others
**There are full unit test cases for showing how it works.**
> **NOTE**:<br>
Before you run the test cases of `kube-resource-server`, please start the `kube-auth-server` first. 
It is an integration test.

#Reference
- https://www.baeldung.com/spring-security-oauth-jwt
## other interesting articles
- https://www.baeldung.com/spring-security-custom-authentication-failure-handler
- https://www.baeldung.com/sso-spring-security-oauth2
- https://www.baeldung.com/spring-security-openid-connect




