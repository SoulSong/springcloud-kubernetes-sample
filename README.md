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
- cloud-kubernetes-config(step 5)
```text
Integrate spring-cloud-kubernetes-config for reloading configurations
```
- redis-rate-limiter(step 6)
```text
Achieve distribute rate limiting by integrating spring-cloud-gateway with redis.
>  token bucket algorithm
```
- master(last step)
```text
Contains all features shown as follow. 
```


## Feature list:
* Integrate spring-cloud-openfeign for native declarative request call
* Integrate spring-cloud-kubernetes-ribbon for discover
* Integrate swagger for API documents
* Integrate spring-cloud-gateway to route and filter backend services
* kubernetes configmap & secret for configurations
* Integrate spring-cloud-kubernetes-config for reloading configurations
* Custom header for api-version
* Add FeignHeaderInterceptor for throughing http-headers into the downstream service
* Aggregate all services' swagger documents into gateway-service
* Distribute rate limiting, using `token bucket algorithm`
* Deploy redis with standalone mode in K8S, and **export access port**
* okHttp3

# How To Build
```bash
$ mvn clean install -P k8s
```
More information can forward to [REAMDE.md](https://github.com/SoulSong/springboot-kubernetes-sample/blob/master/README.md) for detail description of building&deploying and so on. 

# How To Deploy
```bash
$ kubectl apply -f ./kubernetes/namespace.yaml
$ kubectl create secret generic private-secret -n springboot-kube --from-file=privatekey=./kubernetes/privatekey
$ kubectl apply -f ./kubernetes/redis/standalone/
$ kubectl apply -f ./kubernetes/
```

# How To Test
## dev

- request consumer-service directly
```text
$ curl -H "Content-Type:application/json-v1" -H "token:123" localhost:8081/hello/call/producer-service
```

- request consumer-service by gateway-service
```text
$ curl -H "Content-Type:application/json-v1" -H "token:123" localhost:9999/consumer-service/hello/call/producer-service
```

## k8s

- request consumer-service directly
```text
$ curl -H "Content-Type:application/json-v1" -H "token:123" shf.boot.com/consumer-service/hello/call/producer-service
```

- request consumer-service by gateway-service
```text
$ curl -H "Content-Type:application/json-v1" -H "token:123" shf.boot.com/gateway-service/consumer-service/hello/call/producer-service
```


# Config & Secret

## Create base64 info
```bash
  $ echo -n "admin" | base64
  YWRtaW4=
  $ echo -n "123456" | base64
  MTIzNDU2
```

## Test ConfigMap & Secret

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

## Test Config Reload In K8S
- request the reload_properties endpoint
```text
$ curl http://shf.boot.com/consumer-service/reload/config/properties
{"property1":"configmap-reload-property1","property2":"configmap-reload-property2"}
```

- modify the config
```bash
$ kubectl apply -f ./kubernetes/configmap_reload/consumer-configmap-reload.yaml
```

- check the config whether be reloaded?
```text
$ curl http://shf.boot.com/consumer-service/reload/config/properties
{"property1":"configmap-reload1-property1","property2":"configmap-reload1-property2"}
```

# Distribute Rate Limiting
Achieve distribute rate limiting by integrating `spring-cloud-gateway` with `redis`, using the `token bucket algorithm`. 

## Build redis-server in K8S
Here is a [guide document](REDIS_README.md) to introduce how to build a `standalone` redis-server in K8S.

## Test rate limiting
Test the following cases through jmeter, can set the number of threads more than 20.
```case
curl -H "userId:SOUL" shf.boot.com/gateway-service/consumer-service/hello/song
curl -H "userId:SONG" shf.boot.com/gateway-service/consumer-service/hello/song
```
When the rate limiting comes into effect, there will be two phenomena as follows:
- Get keys from redis
> K8S:0>keys request_rate_limiter*
>   1)  "request_rate_limiter.{SONG}.timestamp"
>   2)  "request_rate_limiter.{SOUL}.timestamp"
>   3)  "request_rate_limiter.{SONG}.tokens"
>   4)  "request_rate_limiter.{SOUL}.tokens"

- Some requests could not be responded normally, they failed with response header which contains `429` http-code, means too many requests.
> **HTTP/1.1 429 Too Many Requests**<br>
  Server: nginx/1.15.10<br>
  Date: Sat, 18 May 2019 16:10:54 GMT<br>
  Content-Length: 0<br>
  Connection: keep-alive<br>
  **X-RateLimit-Remaining: 0**<br>
  **X-RateLimit-Burst-Capacity: 10**<br>
  **X-RateLimit-Replenish-Rate: 5**<br>

## Custom another limiting strategy
Here's a lua [script](./kube-gateway-service/src/main/resources/scripts/scroll_window_request_rate_limiter.lua) that implements a window-scrolling limiting strategy.

### Test case
Setting the log level of the redis-server is warning, then execute the script with the command
`redis-cli --eval scroll_window_request_rate_limiter.lua 'foo' , 5 10`. See the detail log:
```text
[8424] 22 Jun 17:44:39.312 # allowed: 1 ; allowed_numer: 4
[8424] 22 Jun 17:44:41.222 # allowed: 1 ; allowed_numer: 3
[8424] 22 Jun 17:44:42.159 # allowed: 1 ; allowed_numer: 2
[8424] 22 Jun 17:44:42.939 # allowed: 1 ; allowed_numer: 1
[8424] 22 Jun 17:44:43.734 # allowed: 1 ; allowed_numer: 0
[8424] 22 Jun 17:44:44.548 # allowed: 0 ; allowed_numer: 0
[8424] 22 Jun 17:44:45.305 # allowed: 0 ; allowed_numer: 0
[8424] 22 Jun 17:44:46.055 # allowed: 0 ; allowed_numer: 0
[8424] 22 Jun 17:44:49.569 # allowed: 1 ; allowed_numer: 4
[8424] 22 Jun 17:44:51.382 # allowed: 1 ; allowed_numer: 3
[8424] 22 Jun 17:44:52.556 # allowed: 1 ; allowed_numer: 2
[8424] 22 Jun 17:44:53.398 # allowed: 1 ; allowed_numer: 1
[8424] 22 Jun 17:44:56.493 # allowed: 1 ; allowed_numer: 0
[8424] 22 Jun 17:44:58.610 # allowed: 0 ; allowed_numer: 0
[8424] 22 Jun 17:45:00.417 # allowed: 1 ; allowed_numer: 4
```
The output is as expected, the window_size is 10 seconds and the capacity is 5.

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


