# Introduction
How to run redis server(standalone mode) in k8s and expose the access port.

# Feature
- config with [configmap](./kubernetes/redis/standalone/redis-configmap.yaml)
- expose access port, see more
   > *  [ingress-nginx-svc.yaml](./kubernetes/redis/standalone/ingress-nginx-svc.yaml)<br>
   > * [tcp-services-configmap.yaml](./kubernetes/redis/standalone/tcp-services-configmap.yaml)

# Run 
```bash
$ kubectl apply -f ./kubernetes/redis/standalone/
```

# Test

## test with pod
```bash
$ kubectl get po -n springboot-kube
NAME                                  READY     STATUS    RESTARTS   AGE
redis-dm-57b9774475-c9wjg             1/1       Running   0          2h

$ kubectl exec -it redis-dm-57b9774475-c9wjg -n springboot-kube redis-cli
127.0.0.1:6379> set a a
OK
127.0.0.1:6379> get a
"a"
```

## test with expose access port
Request from local machine, here is a WIN10 system with a ubuntu subSystem.
```bash
song@songhaifeng:/mnt/d/Redis-x64-4.0.2.3$ ./redis-cli.exe -p 9000
127.0.0.1:9000> get a
"a"
```

# Check config
```bash
$ kubectl exec -it redis-dm-57b9774475-c9wjg -n springboot-kube redis-cli
127.0.0.1:6379> info memory
# Memory
used_memory:897896
used_memory_human:876.85K
used_memory_rss:5570560
used_memory_rss_human:5.31M
used_memory_peak:1001160
used_memory_peak_human:977.70K
used_memory_peak_perc:89.69%
used_memory_overhead:876570
used_memory_startup:791032
used_memory_dataset:21326
used_memory_dataset_perc:19.96%
allocator_allocated:1302928
allocator_active:1581056
allocator_resident:8613888
total_system_memory:10971176960
total_system_memory_human:10.22G
used_memory_lua:49152
used_memory_lua_human:48.00K
used_memory_scripts:1896
used_memory_scripts_human:1.85K
number_of_cached_scripts:1
maxmemory:2097152
maxmemory_human:2.00M
maxmemory_policy:allkeys-lru
allocator_frag_ratio:1.21
allocator_frag_bytes:278128
allocator_rss_ratio:5.45
allocator_rss_bytes:7032832
rss_overhead_ratio:0.65
rss_overhead_bytes:-3043328
mem_fragmentation_ratio:6.50
mem_fragmentation_bytes:4713688
mem_not_counted_for_evict:0
mem_replication_backlog:0
mem_clients_slaves:0
mem_clients_normal:83538
mem_aof_buffer:0
mem_allocator:jemalloc-5.1.0
active_defrag_running:0
lazyfree_pending_objects:0
```

# Reference
-  How to [exposing-tcp-udp-services](https://kubernetes.github.io/ingress-nginx/user-guide/exposing-tcp-udp-services/) with ingress-nginx.
