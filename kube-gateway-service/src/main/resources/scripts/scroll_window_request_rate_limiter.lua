--
-- Created by IntelliJ IDEA.
-- User: songhaifeng
-- Date: 2019/6/22
-- Time: 16:33
-- To change this template use File | Settings | File Templates.
--
-- Window scrolling strategy for request limit

local limit_key = KEYS[1]

local capacity = tonumber(ARGV[1])
-- Unit is senond
local window_size = tonumber(ARGV[2])

--redis.log(redis.LOG_WARNING, "limit_key: " .. limit_key)
--redis.log(redis.LOG_WARNING, "capacity: " .. capacity)
--redis.log(redis.LOG_WARNING, "window_size: " .. window_size)

-- 1 means allowed;0 means not allowed;
local allowed = 1
local allowed_num = capacity

local is_exists = redis.call("EXISTS", limit_key)
if is_exists == 1 then
    local filled_tokens = redis.call("INCR", limit_key);
    if filled_tokens > capacity then
        allowed = 0
        allowed_num = 0
    else
        allowed = 1
        allowed_num = capacity - filled_tokens
    end
else
    -- initialize the window and filled_token number
    redis.call("SETEX", limit_key, window_size, 1)
    allowed = 1
    allowed_num = capacity - 1
end

redis.log(redis.LOG_WARNING, "allowed: " .. allowed .. " ; allowed_numer: " .. allowed_num)
return { allowed, allowed_num }
