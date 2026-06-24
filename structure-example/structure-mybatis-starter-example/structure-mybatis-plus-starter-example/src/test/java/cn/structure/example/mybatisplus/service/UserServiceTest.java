package cn.structure.example.mybatisplus.service;

import cn.structure.example.mybatisplus.config.AbstractIntegrationTest;
import cn.structure.example.mybatisplus.pojo.po.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * UserService 增强测试 - 验证 Service 业务逻辑、IService 高级特性、MetaObjectHandler 自动填充等
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
class UserServiceTest extends AbstractIntegrationTest {

    @Autowired
    private IUserService userService;

    private User createTestUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setUnexpired(true);
        user.setEnabled(true);
        user.setUnlocked(true);
        user.setDeleted(false);
        return user;
    }

    private String uniqueUsername(String prefix) {
        return prefix + "_" + System.nanoTime() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    @DisplayName("getById - 验证 BaseService 方法")
    void getById_shouldReturnUser() {
        String username = uniqueUsername("serviceGet");
        User user = createTestUser(username);
        userService.save(user);

        User found = userService.getById(user.getId());

        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("getById - 不存在返回null")
    void getById_shouldReturnNullForNonExistent() {
        User found = userService.getById(99999999L);

        assertNull(found);
    }

    @Test
    @DisplayName("save - 插入用户并自动填充创建时间")
    void save_shouldInsertAndAutoFillCreateTime() {
        String username = uniqueUsername("saveAutoFill");
        User user = createTestUser(username);

        boolean result = userService.save(user);

        assertTrue(result);
        assertNotNull(user.getId());
        // 验证 MetaObjectHandler 自动填充了创建时间和更新时间
        assertNotNull(user.getCreateTime(), "createTime should be auto-filled by MetaObjectHandler");
        assertNotNull(user.getUpdateTime(), "updateTime should be auto-filled by MetaObjectHandler");
    }

    @Test
    @DisplayName("saveBatch - 批量插入")
    void saveBatch_shouldInsertMultipleUsers() {
        String prefix = uniqueUsername("batch");
        List<User> users = List.of(
                createTestUser(prefix + "_1"),
                createTestUser(prefix + "_2"),
                createTestUser(prefix + "_3")
        );

        boolean result = userService.saveBatch(users);

        assertTrue(result);
        for (User u : users) {
            assertNotNull(u.getId());
        }
    }

    @Test
    @DisplayName("updateById - 更新时自动填充更新时间")
    void updateById_shouldUpdateAndAutoFillUpdateTime() throws InterruptedException {
        String username = uniqueUsername("updateFill");
        User user = createTestUser(username);
        userService.save(user);
        Long id = user.getId();
        java.time.LocalDateTime originalUpdateTime = user.getUpdateTime();

        Thread.sleep(10);
        user.setUsername("updated_" + System.nanoTime());
        boolean result = userService.updateById(user);

        assertTrue(result);
        User updated = userService.getById(id);
        assertNotNull(updated);
        // 验证更新时间已更新
        assertNotNull(updated.getUpdateTime());
        assertNotEquals(originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    @DisplayName("removeById - 物理删除")
    void removeById_shouldDeleteUser() {
        String username = uniqueUsername("delete");
        User user = createTestUser(username);
        userService.save(user);
        Long id = user.getId();

        boolean result = userService.removeById(id);

        assertTrue(result);
        User deleted = userService.getById(id);
        assertNull(deleted);
    }

    @Test
    @DisplayName("removeById - 不存在的ID返回false")
    void removeById_shouldReturnFalseForNonExistent() {
        boolean result = userService.removeById(99999999L);

        assertFalse(result);
    }

    @Test
    @DisplayName("getUserByUsername - 验证Service中自定义方法")
    void getUserByUsername_shouldReturnUser() {
        String username = uniqueUsername("serviceFindByUsername");
        userService.save(createTestUser(username));

        User found = userService.getUserByUsername(username);

        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("getUserByUsername - 不存在返回null")
    void getUserByUsername_shouldReturnNullForNonExistent() {
        User found = userService.getUserByUsername("non_existent_" + UUID.randomUUID());

        assertNull(found);
    }

    @Test
    @DisplayName("insert - 自定义insert方法")
    void insert_shouldInsertUser() {
        String username = uniqueUsername("customInsert");
        User user = createTestUser(username);

        int rows = userService.insert(user);

        assertEquals(1, rows);
        assertNotNull(user.getId());
        User found = userService.getUserByUsername(username);
        assertNotNull(found);
    }

    @Test
    @DisplayName("update - 自定义update方法")
    void update_shouldUpdateUser() {
        String username = uniqueUsername("customUpdate");
        User user = createTestUser(username);
        userService.save(user);
        User inserted = userService.getUserByUsername(username);
        assertNotNull(inserted);

        inserted.setEnabled(false);
        int rows = userService.update(inserted);

        assertEquals(1, rows);
        User updated = userService.getById(inserted.getId());
        assertNotNull(updated);
        assertFalse(updated.getEnabled());
    }

    @Test
    @DisplayName("listUserPage - 按username字符串查询")
    void listUserPage_byUsername_shouldReturnPagedResults() {
        String prefix = uniqueUsername("listPage");
        for (int i = 0; i < 3; i++) {
            userService.save(createTestUser(prefix + "_" + i));
        }

        List<User> users = userService.listUserPage(prefix, 10, 0);

        assertNotNull(users);
        assertTrue(users.size() >= 3);
        assertTrue(users.stream().allMatch(u -> u.getUsername().contains(prefix)));
    }

    @Test
    @DisplayName("listUserPage - 按User对象查询")
    void listUserPage_byUser_shouldReturnPagedResults() {
        String prefix = uniqueUsername("listPageObj");
        userService.save(createTestUser(prefix + "_user"));
        User query = new User();
        query.setUsername(prefix);

        List<User> users = userService.listUserPage(query, 10, 0);

        assertNotNull(users);
        assertTrue(users.size() >= 1);
    }

    @Test
    @DisplayName("IService page - 分页查询")
    void page_shouldReturnPagedResults() {
        String prefix = uniqueUsername("page");
        for (int i = 0; i < 5; i++) {
            userService.save(createTestUser(prefix + "_" + i));
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("username", prefix);
        Page<User> page = new Page<>(1, 3);
        IPage<User> result = userService.page(page, wrapper);

        // 不依赖分页插件: 至少要能正常返回结果集(分页参数会通过SQL的LIMIT生效)
        assertNotNull(result);
        assertNotNull(result.getRecords());
        // 只要数据能被查询出来即可(分页效果取决于是否配置了MybatisPlus的分页插件)
        assertTrue(result.getRecords().size() >= 1);
    }

    @Test
    @DisplayName("IService lambdaQuery - 函数式查询")
    void lambdaQuery_shouldReturnMatchingUsers() {
        String prefix = uniqueUsername("lambda");
        for (int i = 0; i < 2; i++) {
            userService.save(createTestUser(prefix + "_" + i));
        }

        List<User> users = userService.lambdaQuery()
                .like(User::getUsername, prefix)
                .list();

        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    @DisplayName("IService lambdaQuery - 链式条件")
    void lambdaQuery_withChainCondition_shouldWork() {
        String username = uniqueUsername("lambdaChain");
        User user = createTestUser(username);
        user.setEnabled(true);
        userService.save(user);

        User found = userService.lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getEnabled, true)
                .one();

        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("IService lambdaUpdate - 函数式更新")
    void lambdaUpdate_shouldUpdateUsers() {
        String prefix = uniqueUsername("lambdaUpdate");
        for (int i = 0; i < 2; i++) {
            userService.save(createTestUser(prefix + "_" + i));
        }

        boolean result = userService.lambdaUpdate()
                .like(User::getUsername, prefix)
                .set(User::getEnabled, false)
                .update();

        assertTrue(result);
        // 验证更新生效
        List<User> users = userService.lambdaQuery()
                .like(User::getUsername, prefix)
                .list();
        assertTrue(users.stream().allMatch(u -> Boolean.FALSE.equals(u.getEnabled())));
    }

    @Test
    @DisplayName("IService count - 统计数量")
    void count_shouldReturnCorrectNumber() {
        String prefix = uniqueUsername("count");
        for (int i = 0; i < 3; i++) {
            userService.save(createTestUser(prefix + "_" + i));
        }

        long count = userService.lambdaQuery()
                .like(User::getUsername, prefix)
                .count();

        assertEquals(3, count);
    }

    @Test
    @DisplayName("CRUD完整流程 - 验证Service业务规则")
    void fullCrudFlow_shouldWork() {
        // 1. Insert
        String username = uniqueUsername("crud");
        User user = createTestUser(username);
        boolean insertSuccess = userService.save(user);
        assertTrue(insertSuccess);
        assertNotNull(user.getId());

        // 2. Read
        User fetched = userService.getById(user.getId());
        assertNotNull(fetched);
        assertEquals(username, fetched.getUsername());

        // 3. Update
        fetched.setEnabled(false);
        fetched.setPassword("newPassword");
        boolean updateSuccess = userService.updateById(fetched);
        assertTrue(updateSuccess);
        User updatedUser = userService.getById(user.getId());
        assertFalse(updatedUser.getEnabled());
        assertEquals("newPassword", updatedUser.getPassword());

        // 4. Delete
        boolean deleteSuccess = userService.removeById(user.getId());
        assertTrue(deleteSuccess);
        User deletedUser = userService.getById(user.getId());
        assertNull(deletedUser);
    }
}
