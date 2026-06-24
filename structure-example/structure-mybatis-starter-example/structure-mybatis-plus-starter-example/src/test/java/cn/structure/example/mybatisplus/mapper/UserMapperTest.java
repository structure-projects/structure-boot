package cn.structure.example.mybatisplus.mapper;

import cn.structure.example.mybatisplus.config.AbstractIntegrationTest;
import cn.structure.example.mybatisplus.pojo.po.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * UserMapper 增强测试 - 覆盖 MyBatis Plus 基础 CRUD、Wrapper条件查询等功能
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
class UserMapperTest extends AbstractIntegrationTest {

    @Autowired
    private UserMapper userMapper;

    private User createTestUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password123");
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
    @DisplayName("insertUser - XML插入并验证ID生成")
    void insertUser_shouldInsertSuccessfully() {
        String username = uniqueUsername("xmlInsert");
        User user = createTestUser(username);

        int result = userMapper.insertUser(user);

        assertEquals(1, result);
        assertNotNull(user.getId());
        // 验证插入后可通过BaseMapper查出来
        User found = userMapper.selectById(user.getId());
        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("updateUser - XML动态更新")
    void updateUser_shouldUpdateSuccessfully() {
        String username = uniqueUsername("xmlUpdate");
        User user = createTestUser(username);
        userMapper.insertUser(user);
        Long id = user.getId();

        user.setUsername("xmlUpdated_" + System.nanoTime());
        user.setEnabled(false);
        int result = userMapper.updateUser(user);

        assertEquals(1, result);
        User updated = userMapper.selectById(id);
        assertNotNull(updated);
        assertFalse(updated.getEnabled());
    }

    @Test
    @DisplayName("getUserByUsername - 成功")
    void getUserByUsername_shouldReturnUser() {
        String username = uniqueUsername("findByUsername");
        userMapper.insertUser(createTestUser(username));

        User found = userMapper.getUserByUsername(username);

        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("getUserByUsername - 不存在返回null")
    void getUserByUsername_shouldReturnNullWhenNotFound() {
        User found = userMapper.getUserByUsername("non_existent_" + UUID.randomUUID());

        assertNull(found);
    }

    @Test
    @DisplayName("getUserByUsername - null参数不抛异常")
    void getUserByUsername_withNull_shouldNotThrow() {
        User found = userMapper.getUserByUsername(null);

        assertNull(found);
    }

    @Test
    @DisplayName("listUserPage - 分页查询")
    void listUserPage_shouldReturnPagedResults() {
        String prefix = uniqueUsername("mpPage");
        for (int i = 0; i < 3; i++) {
            userMapper.insertUser(createTestUser(prefix + "_" + i));
        }

        User query = new User();
        List<User> users = userMapper.listUserPage(query, 3, 0);

        assertNotNull(users);
        assertTrue(users.size() <= 3);
    }

    @Test
    @DisplayName("listUserPage - 按用户名过滤")
    void listUserPage_withUsernameFilter_shouldFilterResults() {
        String uniqueKey = uniqueUsername("filter");
        userMapper.insertUser(createTestUser(uniqueKey + "_a"));
        userMapper.insertUser(createTestUser(uniqueKey + "_b"));
        userMapper.insertUser(createTestUser("other_" + UUID.randomUUID()));

        User query = new User();
        query.setUsername(uniqueKey);
        List<User> users = userMapper.listUserPage(query, 10, 0);

        assertNotNull(users);
        assertTrue(users.size() >= 2);
        assertTrue(users.stream().allMatch(u -> u.getUsername().contains(uniqueKey)));
    }

    @Test
    @DisplayName("listUserPage - 边界pageSize=0不抛异常")
    void listUserPage_withZeroPageSize_shouldNotThrow() {
        User query = new User();
        List<User> users = userMapper.listUserPage(query, 0, 0);

        assertNotNull(users);
    }

    @Test
    @DisplayName("BaseMapper selectById - 验证基础CRUD")
    void selectById_shouldReturnUser() {
        String username = uniqueUsername("baseMapper");
        userMapper.insertUser(createTestUser(username));
        User inserted = userMapper.getUserByUsername(username);
        assertNotNull(inserted);

        User found = userMapper.selectById(inserted.getId());

        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("BaseMapper selectById - 不存在返回null")
    void selectById_shouldReturnNullForNonExistent() {
        User found = userMapper.selectById(99999999L);

        assertNull(found);
    }

    @Test
    @DisplayName("QueryWrapper条件查询 - eq username")
    void selectByQueryWrapper_eq_shouldFilterResults() {
        String username = uniqueUsername("wrapper");
        userMapper.insertUser(createTestUser(username));

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        List<User> users = userMapper.selectList(wrapper);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(username, users.get(0).getUsername());
    }

    @Test
    @DisplayName("QueryWrapper条件查询 - like username")
    void selectByQueryWrapper_like_shouldFilterResults() {
        String uniqueKey = uniqueUsername("like");
        userMapper.insertUser(createTestUser(uniqueKey + "_a"));
        userMapper.insertUser(createTestUser(uniqueKey + "_b"));
        userMapper.insertUser(createTestUser("other_" + UUID.randomUUID()));

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("username", uniqueKey);
        List<User> users = userMapper.selectList(wrapper);

        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    @DisplayName("QueryWrapper条件查询 - 复杂组合条件")
    void selectByQueryWrapper_complexCondition_shouldWork() {
        String username = uniqueUsername("complex");
        User user = createTestUser(username);
        user.setEnabled(true);
        userMapper.insertUser(user);

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 注意: User 实体 @TableField("is_enabled") 对应数据库列 is_enabled
        wrapper.eq("is_enabled", true)
                .like("username", uniqueKey(username))
                .orderByDesc("id");
        List<User> users = userMapper.selectList(wrapper);

        assertNotNull(users);
        assertTrue(users.size() >= 1);
    }

    @Test
    @DisplayName("UpdateWrapper条件更新 - 不通过ID更新")
    void updateByUpdateWrapper_shouldUpdate() {
        String username = uniqueUsername("updateWrapper");
        userMapper.insertUser(createTestUser(username));
        User inserted = userMapper.getUserByUsername(username);
        assertNotNull(inserted);

        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("username", username)
                .set("password", "newPassViaWrapper");
        int rows = userMapper.update(null, wrapper);

        assertEquals(1, rows);
        User updated = userMapper.getUserByUsername(username);
        assertNotNull(updated);
        assertEquals("newPassViaWrapper", updated.getPassword());
    }

    @Test
    @DisplayName("BaseMapper selectCount - 统计数量")
    void selectCount_shouldReturnCount() {
        String prefix = uniqueUsername("count");
        for (int i = 0; i < 3; i++) {
            userMapper.insertUser(createTestUser(prefix + "_" + i));
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("username", prefix);
        Long count = userMapper.selectCount(wrapper);

        assertNotNull(count);
        assertEquals(3, count);
    }

    @Test
    @DisplayName("BaseMapper selectList - 空结果集返回空列表")
    void selectList_withNoMatch_shouldReturnEmptyList() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", "no_such_user_" + UUID.randomUUID());
        List<User> users = userMapper.selectList(wrapper);

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    private String uniqueKey(String s) {
        return s.substring(0, Math.min(s.length(), 30));
    }
}
