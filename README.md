# Introduction
Builds on [springboot-kubernetes-sample](https://github.com/SoulSong/springboot-kubernetes-sample) project.  

##Add feature list:
* ribbon
* feign
* custom header
* add FeignHeaderInterceptor for throughing http-header into the downstream service


# How To Test
Forward to [REAMDE.md](https://github.com/SoulSong/springboot-kubernetes-sample/blob/master/README.md) for detail description of building&deploying and so on. 

## dev
```text
curl -H "Content-Type:application/json;v1" -H "token:123" localhost:8081/call/producer-service
```

## k8s
```text
curl -H "Content-Type:application/json;v1" -H "token:123" shf.boot.com/consumer-service/call/producer-service
```
