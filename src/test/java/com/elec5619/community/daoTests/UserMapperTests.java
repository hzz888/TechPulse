package com.elec5619.community.daoTests;

import com.elec5619.community.dao.UserMapper;
import com.elec5619.community.entity.User;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = com.elec5619.community.CommunityApplication.class)
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectById() {
        User testUser = userMapper.selectById(1);
        assertNotNull(testUser);
    }

    @Test
    public void testSelectByName() {
        User testUser = userMapper.selectByName("test");
        assertNotNull(testUser);
    }

    @Test
    public void testSelectByEmail() {
        User testUser = userMapper.selectByEmail("test@mail.com");
        assertNotNull(testUser);
    }
}
