package cn.structure.example.mybatis.service;

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
 * User Service 业务逻辑测试
 * </p>
 * <p>
 * 覆盖范围: 业务规则的正确执行、参数处理、返回数据的有效性
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
class UserServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private IUserService iUserService;

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
    @DisplayName("getUserById - 存在的用户返回正确信息")
    void getUserById_shouldReturnUser() {
        String username = uniqueUsername("service_get");
        iUserService.insertUser(createTestUser(username));
        User fetched = iUserService.getUserByUsername(username);
        assertNotNull(fetched);
        Long id = fetched.getId();

        User found = iUserService.getUserById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("getUserById - 不存在的用户返回null")
    void getUserById_shouldReturnNullForNonExistentUser() {
        User found = iUserService.getUserById(99999999L);

        assertNull(found);
    }

    @Test
    @DisplayName("getUserByUsername - 按用户名查询成功")
    void getUserByUsername_shouldReturnUser() {
        String username = uniqueUsername("service_find");
        iUserService.insertUser(createTestUser(username));

        User found = iUserService.getUserByUsername(username);

        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("getUserByUsername - 不存在的用户名返回null")
    void getUserByUsername_shouldReturnNullForNonExistent() {
        User found = iUserService.getUserByUsername("non_existent_" + UUID.randomUUID());

        assertNull(found);
    }

    @Test
    @DisplayName("getUserByUsername - 用户名为null不抛异常")
    void getUserByUsername_withNull_shouldNotThrow() {
        User found = iUserService.getUserByUsername(null);

        assertNull(found);
    }

    @Test
    @DisplayName("insertUser - 插入成功并返回影响行数")
    void insertUser_shouldInsertAndReturnRows() {
        String username = uniqueUsername("service_insert");
        User user = createTestUser(username);

        int rows = iUserService.insertUser(user);

        assertEquals(1, rows);
        // 通过唯一用户名验证确实插入成功
        User found = iUserService.getUserByUsername(username);
        assertNotNull(found);
    }

    @Test
    @DisplayName("updateUserById - 更新成功")
    void updateUserById_shouldUpdateSuccessfully() {
        String username = uniqueUsername("service_update");
        iUserService.insertUser(createTestUser(username));
        User fetched = iUserService.getUserByUsername(username);
        assertNotNull(fetched);
        Long id = fetched.getId();

        fetched.setPassword("updatedPassword");
        int rows = iUserService.updateUserById(fetched);

        assertEquals(1, rows);
        User updated = iUserService.getUserById(id);
        assertNotNull(updated);
        assertEquals("updatedPassword", updated.getPassword());
    }

    @Test
    @DisplayName("updateUserById - 不存在的ID返回0")
    void updateUserById_shouldReturnZeroForNonExistent() {
        User user = createTestUser(uniqueUsername("service_update_none"));
        user.setId(99999999L);

        int rows = iUserService.updateUserById(user);

        assertEquals(0, rows);
    }

    @Test
    @DisplayName("deleteById - 删除成功")
    void deleteById_shouldDeleteSuccessfully() {
        String username = uniqueUsername("service_delete");
        iUserService.insertUser(createTestUser(username));
        User fetched = iUserService.getUserByUsername(username);
        assertNotNull(fetched);
        Long id = fetched.getId();

        int rows = iUserService.deleteById(id);

        assertEquals(1, rows);
        User deleted = iUserService.getUserById(id);
        assertNull(deleted);
    }

    @Test
    @DisplayName("deleteById - 不存在的ID返回0")
    void deleteById_shouldReturnZeroForNonExistent() {
        int rows = iUserService.deleteById(99999999L);

        assertEquals(0, rows);
    }

    @Test
    @DisplayName("listUserPage - 返回分页结果")
    void listUserPage_shouldReturnPagedResults() {
        String prefix = uniqueUsername("service_page");
        for (int i = 0; i < 3; i++) {
            iUserService.insertUser(createTestUser(prefix + "_" + i));
        }

        List<User> users = iUserService.listUserPage(prefix, 10, 0);

        assertNotNull(users);
        assertTrue(users.size() >= 3);
        assertTrue(users.stream().allMatch(u -> u.getUsername().contains(prefix)));
    }

    @Test
    @DisplayName("listUserPage - 按用户名过滤")
    void listUserPage_shouldFilterByUsername() {
        String prefix = uniqueUsername("filter");
        iUserService.insertUser(createTestUser(prefix + "_a"));
        iUserService.insertUser(createTestUser(prefix + "_b"));
        iUserService.insertUser(createTestUser("other_" + UUID.randomUUID()));

        List<User> users = iUserService.listUserPage(prefix, 10, 0);

        assertNotNull(users);
        assertTrue(users.size() >= 2);
        assertTrue(users.stream().allMatch(u -> u.getUsername().startsWith(prefix)));
    }

    @Test
    @DisplayName("listUserPage - 边界pageSize=0不抛异常")
    void listUserPage_withZeroPageSize_shouldNotThrow() {
        List<User> users = iUserService.listUserPage("", 0, 0);

        assertNotNull(users);
    }

    @Test
    @DisplayName("listUserPage - username为null不抛异常")
    void listUserPage_withNullUsername_shouldNotThrow() {
        List<User> users = iUserService.listUserPage(null, 10, 0);

        assertNotNull(users);
    }

    @Test
    @DisplayName("CRUD完整流程 - 验证业务规则")
    void fullCrudFlow_shouldWork() {
        // 1. Insert
        String username = uniqueUsername("crud");
        User user = createTestUser(username);
        int inserted = iUserService.insertUser(user);
        assertEquals(1, inserted);

        // 2. Read
        User fetched = iUserService.getUserByUsername(username);
        assertNotNull(fetched);
        assertEquals(username, fetched.getUsername());
        assertTrue(fetched.getEnabled());
        assertTrue(fetched.getUnexpired());

        // 3. Update
        fetched.setEnabled(false);
        fetched.setPassword("newPass");
        int updated = iUserService.updateUserById(fetched);
        assertEquals(1, updated);

        User updatedUser = iUserService.getUserById(fetched.getId());
        assertFalse(updatedUser.getEnabled());
        assertEquals("newPass", updatedUser.getPassword());

        // 4. Delete
        int deleted = iUserService.deleteById(fetched.getId());
        assertEquals(1, deleted);

        User deletedUser = iUserService.getUserById(fetched.getId());
        assertNull(deletedUser);
    }
}
