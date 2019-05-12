# Introduction
A spring-cloud-kubernetes sample project builds on [springboot-kubernetes-sample](https://github.com/SoulSong/springboot-kubernetes-sample) project.
You can try k8s integrating with spring-cloud step by step, with all branches as follow. 

## Differences of all branches:
- openfeign(step 1)
```text
Only integrate openfeign without ribbon.
```
- ribbon-feign(step 2)
```text
Integrate openfeign with spring-cloud-ribbon.
```
- kubernetes-ribbon-feign(step 3)
```text
Integrate openfeign with spring-cloud-kubernetes-ribbon.
```
- configmap-secret(step 4)
```text
Add kubernetes configmap & secret feature for configuration. See consumer-service for more detail. 
```

## Feature list:
* spring-cloud-openfeign
* spring-cloud-kubernetes-ribbon
* okHttp3
* swagger
* spring-cloud-gateway
* kubernetes configmap & secret for configuration
* Custom header for api-version
* Add FeignHeaderInterceptor for throughing http-headers into the downstream service
* Aggregate all services' swagger config into gateway-service


# How To Build & Deploy
```bash
mvn clean install -P k8s
cd kubernetes && kubectl apply -f .
```
More information can forward to [REAMDE.md](https://github.com/SoulSong/springboot-kubernetes-sample/blob/master/README.md) for detail description of building&deploying and so on. 


# How To Test
## dev

- request consumer-service directly
```text
curl -H "Content-Type:application/json-v1" -H "token:123" localhost:8081/hello/call/producer-service
```

- request consumer-service by gateway-service
```text
curl -H "Content-Type:application/json-v1" -H "token:123" localhost:9999/consumer-service/hello/call/producer-service
```

## k8s

- request consumer-service directly
```text
curl -H "Content-Type:application/json-v1" -H "token:123" shf.boot.com/consumer-service/hello/call/producer-service
```

- request consumer-service by gateway-service
```text
curl -H "Content-Type:application/json-v1" -H "token:123" shf.boot.com/gateway-service/consumer-service/hello/call/producer-service
```


# Config & Secret

## Create base64 info
```bash
  $ echo -n "admin" | base64
  YWRtaW4=
  $ echo -n "123456" | base64
  MTIzNDU2
```

## How To Test ConfigMap & Secret

### dev

- test secret ref
```text
$ curl 127.0.0.1:8081/secret
mongodb://admin:654321@mongodb/admin
```

- test config ref
```text
$ curl 127.0.0.1:8081/single/config/variable
properties-variable0
```

- test batch config ref
```text
$ curl 127.0.0.1:8081/batch/config/variables
{"variable1":"properties-variable1","variable2":"properties-variable2"}
```

### k8s

- test secret ref
```text
$ curl http://shf.boot.com/gateway-service/consumer-service/secret
mongodb://admin:123456@mongodb/test
```

- test config ref
```text
$ curl http://shf.boot.com/gateway-service/consumer-service/single/config/variable
configmap-variable0
```

- test batch config ref
```text
$ curl http://shf.boot.com/gateway-service/consumer-service/batch/config/variables
{"variable1":"configmap-variable1","variable2":"configmap-variable2"}
```

## Test Secret Volume In K8S
- create secret
```bash
$ kubectl create secret generic private-secret -n springboot-kube --from-file=privatekey=./kubernetes/privatekey
secret "private-secret" created
```

- check the mounted directory and data in the mounted file
```bash
$ kubectl exec $(kubectl get po -n springboot-kube | grep 'consumer' | awk  'NR==1{print $1}') -n springboot-kube -c consumer-service ls /etc/
$ kubectl exec $(kubectl get po -n springboot-kube | grep 'consumer' | awk  'NR==1{print $1}') -n springboot-kube -c consumer-service cat /etc/private-key.key
abc
```


# How To Use Swagger
## dev
- consumer-service
```text
http://127.0.0.1:8081/swagger-ui.html
```
![avatar](./doc/img/dev-consumer-swagger.jpg)

- producer-service
```text
http://127.0.0.1:8080/swagger-ui.html
```
![avatar](./doc/img/dev-producer-swagger.jpg)

## k8s
- gateway-service

Aggregate all swagger info for all services.
```text
http://shf.boot.com/gateway-service/swagger-ui.html
```
![avatar](./doc/img/k8s-gateway-swagger.jpg)


