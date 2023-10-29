package com.elec5619.community.daoTests;

import com.elec5619.community.entity.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import com.elec5619.community.dao.LoginTicketMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = com.elec5619.community.CommunityApplication.class)
public class LoginTicketMapperTests {
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testSelectByTicket() {
        assertNotNull(loginTicketMapper.selectByTicket("abc"));
    }

    @Test
    public void testUpdateStatus() {
        assertEquals(1, loginTicketMapper.updateStatus("abc", 1));
    }

    @Test
    public void testInsertLoginTicket() {
        LoginTicket testLoginTicket= new LoginTicket();
        testLoginTicket.setUserId(1);
        testLoginTicket.setTicket("abc");
        testLoginTicket.setStatus(0);
        testLoginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        assertEquals(1, loginTicketMapper.insertLoginTicket(testLoginTicket));
    }

}
