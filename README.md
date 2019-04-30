# Introduction
Builds on [springboot-kubernetes-sample](https://github.com/SoulSong/springboot-kubernetes-sample) project, 
Replace `spring-cloud-starter-kubernetes-ribbon` dependency with `spring-cloud-starter-netflix-ribbon`.


##Add feature list:
* spring-cloud-kubernetes-ribbon
* spring-cloud-openfeign
* okHttp3
* swagger
* custom header for api-version
* add FeignHeaderInterceptor for throughing http-headers into the downstream service

# How To Build & Deploy
```bash
mvn clean install -P k8s
cd kubernetes && kubectl apply -f .
```
More information can forward to [REAMDE.md](https://github.com/SoulSong/springboot-kubernetes-sample/blob/master/README.md) for detail description of building&deploying and so on. 

# How To Test
## dev
```text
curl -H "Content-Type:application/json-v1" -H "token:123" localhost:8081/hello/call/producer-service
```

## k8s
```text
curl -H "Content-Type:application/json-v1" -H "token:123" shf.boot.com/hello/consumer-service/call/producer-service
```

