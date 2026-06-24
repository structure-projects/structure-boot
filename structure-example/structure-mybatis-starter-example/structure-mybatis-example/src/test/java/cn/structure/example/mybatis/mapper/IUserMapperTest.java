package cn.structure.example.mybatis.mapper;

import cn.structure.example.mybatis.config.AbstractIntegrationTest;
import cn.structure.example.mybatis.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * User Mapper 集成测试
 * </p>
 * <p>
 * 覆盖范围: 增、删、改、查、动态SQL、分表等核心功能
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
class IUserMapperTest extends AbstractIntegrationTest {

    @Autowired
    private IUserMapper iUserMapper;

    private User createTestUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password123");
        user.setUnexpired(true);
        user.setEnabled(true);
        user.setUnlocked(true);
        user.setDeleted(false);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return user;
    }

    private String uniqueUsername(String prefix) {
        return prefix + "_" + System.nanoTime() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    @DisplayName("通过ID查询用户 - 成功")
    void getUserById_shouldReturnUser() {
        String username = uniqueUsername("getById");
        User user = createTestUser(username);
        iUserMapper.insertUser(user);

        // 插入时未配置 useGeneratedKeys,需要先按用户名查到ID
        User fetched = iUserMapper.listUser(username);

        assertNotNull(fetched);
        assertEquals(username, fetched.getUsername());

        // 通过ID再查询一次
        User found = iUserMapper.getUserById(fetched.getId());

        assertNotNull(found);
        assertEquals(fetched.getId(), found.getId());
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("通过ID查询用户 - 不存在时返回null")
    void getUserById_shouldReturnNullWhenNotFound() {
        User found = iUserMapper.getUserById(9999999L);

        assertNull(found);
    }

    @Test
    @DisplayName("通过ID查询用户 - 边界ID为null")
    void getUserById_withNullId_shouldNotThrow() {
        User found = iUserMapper.getUserById(null);

        assertNull(found);
    }

    @Test
    @DisplayName("通过用户名查询用户 - 成功")
    void listUser_shouldReturnUser() {
        String username = uniqueUsername("findByName");
        iUserMapper.insertUser(createTestUser(username));

        User found = iUserMapper.listUser(username);

        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("通过用户名查询用户 - 不存在时返回null")
    void listUser_shouldReturnNullWhenNotFound() {
        User found = iUserMapper.listUser("nonexistent_user_" + UUID.randomUUID());

        assertNull(found);
    }

    @Test
    @DisplayName("通过用户名查询用户 - 用户名为null")
    void listUser_withNullUsername_shouldNotThrow() {
        User found = iUserMapper.listUser(null);

        assertNull(found);
    }

    @Test
    @DisplayName("通过用户名查询用户 - 用户名为空字符串")
    void listUser_withEmptyUsername_shouldNotThrow() {
        User found = iUserMapper.listUser("");

        assertNull(found);
    }

    @Test
    @DisplayName("插入用户 - 成功")
    void insertUser_shouldInsertSuccessfully() {
        String username = uniqueUsername("insert");
        User user = createTestUser(username);

        int rows = iUserMapper.insertUser(user);

        assertEquals(1, rows);
        // 通过唯一用户名验证确实插入成功
        User found = iUserMapper.listUser(username);
        assertNotNull(found);
    }

    @Test
    @DisplayName("分表插入用户 - 成功")
    void insertUserSplitTable_shouldInsertSuccessfully() {
        String username = uniqueUsername("splitTable");
        User user = createTestUser(username);

        int rows = iUserMapper.insertUserSplitTable(user);

        assertEquals(1, rows);
        // 通过唯一用户名验证确实插入成功
        User found = iUserMapper.listUser(username);
        assertNotNull(found);
    }

    @Test
    @DisplayName("更新用户 - 动态修改密码")
    void updateUserById_shouldUpdatePassword() {
        String username = uniqueUsername("update");
        User user = createTestUser(username);
        iUserMapper.insertUser(user);
        User fetched = iUserMapper.listUser(username);
        assertNotNull(fetched);

        fetched.setPassword("new_password_456");
        int rows = iUserMapper.updateUserById(fetched);

        assertEquals(1, rows);
        User updated = iUserMapper.getUserById(fetched.getId());
        assertNotNull(updated);
        assertEquals("new_password_456", updated.getPassword());
    }

    @Test
    @DisplayName("更新用户 - 禁用账户")
    void updateUserById_shouldDisableUser() {
        String username = uniqueUsername("disable");
        iUserMapper.insertUser(createTestUser(username));
        User fetched = iUserMapper.listUser(username);
        assertNotNull(fetched);
        Long id = fetched.getId();

        fetched.setEnabled(false);
        int rows = iUserMapper.updateUserById(fetched);

        assertEquals(1, rows);
        User updated = iUserMapper.getUserById(id);
        assertNotNull(updated);
        assertFalse(updated.getEnabled());
    }

    @Test
    @DisplayName("更新用户 - 锁定账户")
    void updateUserById_shouldLockUser() {
        String username = uniqueUsername("lock");
        iUserMapper.insertUser(createTestUser(username));
        User fetched = iUserMapper.listUser(username);
        assertNotNull(fetched);
        Long id = fetched.getId();

        fetched.setUnlocked(false);
        int rows = iUserMapper.updateUserById(fetched);

        assertEquals(1, rows);
        User updated = iUserMapper.getUserById(id);
        assertNotNull(updated);
        assertFalse(updated.getUnlocked());
    }

    @Test
    @DisplayName("更新用户 - 标记删除")
    void updateUserById_shouldMarkDeleted() {
        String username = uniqueUsername("markDeleted");
        iUserMapper.insertUser(createTestUser(username));
        User fetched = iUserMapper.listUser(username);
        assertNotNull(fetched);
        Long id = fetched.getId();

        fetched.setDeleted(true);
        int rows = iUserMapper.updateUserById(fetched);

        assertEquals(1, rows);
        User updated = iUserMapper.getUserById(id);
        assertNotNull(updated);
        assertTrue(updated.getDeleted());
    }

    @Test
    @DisplayName("更新用户 - 不存在的ID应该影响0行")
    void updateUserById_withNonExistentId_shouldAffectZeroRows() {
        User user = createTestUser(uniqueUsername("update_nonexist"));
        user.setId(99999999L);

        int rows = iUserMapper.updateUserById(user);

        assertEquals(0, rows);
    }

    @Test
    @DisplayName("删除用户 - 成功")
    void delete_shouldRemoveUser() {
        String username = uniqueUsername("delete");
        iUserMapper.insertUser(createTestUser(username));
        User fetched = iUserMapper.listUser(username);
        assertNotNull(fetched);
        Long id = fetched.getId();

        int rows = iUserMapper.delete(id);

        assertEquals(1, rows);
        User deleted = iUserMapper.getUserById(id);
        assertNull(deleted);
    }

    @Test
    @DisplayName("删除用户 - 不存在的ID应该影响0行")
    void delete_withNonExistentId_shouldAffectZeroRows() {
        int rows = iUserMapper.delete(99999999L);

        assertEquals(0, rows);
    }

    @Test
    @DisplayName("分页查询 - 无过滤条件")
    void listUserPage_shouldReturnPagedResults() {
        String prefix = uniqueUsername("page");
        for (int i = 0; i < 3; i++) {
            iUserMapper.insertUser(createTestUser(prefix + "_" + i));
        }

        User query = new User();
        query.setUsername(prefix);
        List<User> users = iUserMapper.listUserPage(query, 10, 0);

        assertNotNull(users);
        assertTrue(users.size() >= 3);
        assertTrue(users.stream().allMatch(u -> u.getUsername().contains(prefix)));
    }

    @Test
    @DisplayName("分页查询 - 按用户名模糊过滤")
    void listUserPage_withUsernameFilter_shouldFilterResults() {
        String uniqueKey = uniqueUsername("filter");
        iUserMapper.insertUser(createTestUser(uniqueKey + "_a"));
        iUserMapper.insertUser(createTestUser(uniqueKey + "_b"));
        iUserMapper.insertUser(createTestUser("other_" + UUID.randomUUID()));

        User query = new User();
        query.setUsername(uniqueKey);
        List<User> users = iUserMapper.listUserPage(query, 10, 0);

        assertNotNull(users);
        assertTrue(users.size() >= 2);
        assertTrue(users.stream().allMatch(u -> u.getUsername().contains(uniqueKey)));
    }

    @Test
    @DisplayName("分页查询 - 边界pageSize=0")
    void listUserPage_withZeroPageSize_shouldHandleGracefully() {
        User query = new User();
        List<User> users = iUserMapper.listUserPage(query, 0, 0);

        assertNotNull(users);
    }

    @Test
    @DisplayName("分页查询 - 边界offset大于总数")
    void listUserPage_withLargeOffset_shouldReturnEmpty() {
        User query = new User();
        List<User> users = iUserMapper.listUserPage(query, 10, 99999);

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }
}
