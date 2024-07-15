--- -1 failed
--- 1 success

--- getLock key
local result = redis.call('setnx' , KEYS[1] , ARGV[1])
if result == 1 then
    --PEXPIRE:以毫秒的形式指定过期时间
    redis.call('pexpire' , KEYS[1] , ARGV[2])
else
    result = -1;
    -- 如果value相同，则认为是同一个线程的请求，则认为重入锁
    local value = redis.call('get' , KEYS[1])
    if (value == ARGV[1]) then
        result = 1;
        redis.call('pexpire' , KEYS[1] , ARGV[2])
    end
end
--  如果获取锁成功，则返回 1
return result