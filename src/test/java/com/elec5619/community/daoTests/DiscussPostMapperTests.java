package com.elec5619.community.daoTests;

import com.elec5619.community.dao.DiscussPostMapper;
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
public class DiscussPostMapperTests {
    @Autowired
    DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectDiscussPosts() {
        assertNotNull(discussPostMapper.selectDiscussPosts(0, null,0, 10));
    }

    @Test
    public void testSelectDiscussPostRows() {
        assert(discussPostMapper.selectDiscussPostRows(0,null) >= 0);
    }
}
