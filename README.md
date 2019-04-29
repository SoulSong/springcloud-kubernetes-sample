# Introduction
Builds on [`ribbon-feign`](https://github.com/SoulSong/springboot-kubernetes-sample) branch.

##Add feature list:
* Replace `spring-cloud-starter-kubernetes-ribbon` dependency with `spring-cloud-starter-netflix-ribbon`


# How To Build & Deploy
```bash
mvn clean install -P k8s
cd kubernetes && kubectl apply -f .
```
More information can forward to [REAMDE.md](https://github.com/SoulSong/springboot-kubernetes-sample/blob/master/README.md) for detail description of building&deploying and so on. 

# How To Test
## dev
```text
curl -H "Content-Type:application/json;v1" -H "token:123" localhost:8081/call/producer-service
```

## k8s
```text
curl -H "Content-Type:application/json;v1" -H "token:123" shf.boot.com/consumer-service/call/producer-service
```
