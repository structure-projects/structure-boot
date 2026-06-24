package cn.structure.example.redis.service;

import cn.structure.example.redis.entity.RedisLockBo;
import cn.structure.starter.redis.lock.IDistributedLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisLockServiceTest {

    @Mock
    private IDistributedLock iDistributedLock;

    @InjectMocks
    private RedisLockService redisLockService;

    @Test
    void redisLock_withStringKey_shouldExecute() {
        assertDoesNotThrow(() -> redisLockService.redisLock("testKey"));
    }

    @Test
    void redisLock_withBo_shouldExecute() {
        RedisLockBo bo = new RedisLockBo();
        bo.setKey("boKey");
        bo.setType("type1");

        assertDoesNotThrow(() -> redisLockService.redisLock(bo));
    }

    @Test
    void redisLock_withBoAndKey_shouldExecute() {
        RedisLockBo bo = new RedisLockBo();
        bo.setKey("boKey");

        assertDoesNotThrow(() -> redisLockService.redisLock(bo, "extraKey"));
    }

    @Test
    void redisLock_manualLockSuccess_shouldReleaseLock() {
        when(iDistributedLock.lock("123456")).thenReturn(true);

        redisLockService.redisLock();

        verify(iDistributedLock).lock("123456");
        verify(iDistributedLock).releaseLock("123456");
    }

    @Test
    void redisLock_manualLockFailed_shouldNotReleaseLock() {
        when(iDistributedLock.lock("123456")).thenReturn(false);

        redisLockService.redisLock();

        verify(iDistributedLock).lock("123456");
        verify(iDistributedLock, org.mockito.Mockito.never()).releaseLock("123456");
    }
}
