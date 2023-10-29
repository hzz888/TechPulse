package com.elec5619.community.service;

import com.elec5619.community.dao.LoginTicketMapper;
import com.elec5619.community.dao.UserMapper;
import com.elec5619.community.entity.LoginTicket;
import com.elec5619.community.entity.User;
import com.elec5619.community.util.CommunityConstant;
import com.elec5619.community.util.CommunityUtils;
import com.elec5619.community.util.MailClient;
import com.elec5619.community.util.RedisKeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.elec5619.community.util.CommunityConstant.*;

/**
 * @author Zhengzuo Huo
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private RedisTemplate redisTemplate;

    public User findUserById(int id) {
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }
    private User getCache(int userId) {
        String redisKey = RedisKeyUtils.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    public User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtils.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    public void clearCache(int userId) {
        String redisKey = RedisKeyUtils.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    public Map<String, Object> register(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "Username cannot be empty");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword()) || user.getPassword().length() < 8) {
            map.put("passwordMsg", "Password must be at least 8 characters");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "Email cannot be empty");
            return map;
        }

        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "Username already exists");
            return map;
        }

        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "Email already used");
            return map;
        }

        user.setSalt(CommunityUtils.generateUUID().substring(0, 5));

        user.setPassword(CommunityUtils.md5(user.getPassword() + user.getSalt()));

        user.setType(0);

        user.setStatus(0);

        user.setActivationCode(CommunityUtils.generateUUID().substring(0, 6));

        user.setHeaderUrl(domain + contextPath + "/img/default-avatar.png");

        user.setCreateTime(new Date());

        userMapper.insertUser(user);

        // Send activation email
        Context context = new Context();

        context.setVariable("email", user.getEmail());

        context.setVariable("url", domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode());

        String content = templateEngine.process("/mail/activation", context);

        mailClient.sendMail(user.getEmail(), "Activation", content);

        return map;
    }

    public int activation(int userId, String code){

        User user = userMapper.selectById(userId);

        if (user.getStatus() == 1) {
            return CommunityConstant.ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return CommunityConstant.ACTIVATION_SUCCESS;
        } else {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "Username cannot be empty");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "Password cannot be empty");
            return map;
        }

        User user = userMapper.selectByName(username);

        if (user == null) {
            map.put("usernameMsg", "User does not exist");
            return map;
        }

        if (user.getStatus() == 0) {
            map.put("usernameMsg", "User has not been activated");
            return map;
        }

        password = CommunityUtils.md5(password + user.getSalt());

        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "Password is incorrect");
            return map;
        }

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtils.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }


    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }

    public Map<String, Object> updatePassword(User user, String oldPassword, String newPassword, String confirmPassword) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(oldPassword)) {
            map.put("oldPasswordMsg", "Password cannot be empty");
            return map;
        }
        if (StringUtils.isBlank(newPassword) || newPassword.length() < 8) {
            map.put("newPasswordMsg", "Password must be at least 8 characters");
            return map;
        }
        if (!newPassword.equals(confirmPassword)) {
            map.put("confirmPasswordMsg", "Passwords do not match");
            return map;
        }

        oldPassword = CommunityUtils.md5(oldPassword + user.getSalt());

        if (!user.getPassword().equals(oldPassword)) {
            map.put("oldPasswordMsg", "Wrong Password");
            return map;
        }
        newPassword = CommunityUtils.md5(newPassword + user.getSalt());
        userMapper.updatePassword(user.getId(), newPassword);
        return map;
    }


    public Map<String , Object> resetPassword(String email, String password, String confirmPassword) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(email)) {
            map.put("emailMsg", "Email cannot be empty");
            return map;
        }
        if (StringUtils.isBlank(password) || password.length() < 8) {
            map.put("passwordMsg", "Password must be at least 8 characters");
            return map;
        }

        if (!password.equals(confirmPassword)) {
            map.put("confirmPasswordMsg", "Passwords do not match");
            return map;
        }

        User user = userMapper.selectByEmail(email);
        if (user == null) {
            map.put("emailMsg", "Email does not exist");
            return map;
        }
        password = CommunityUtils.md5(password + user.getSalt());
        userMapper.updatePassword(user.getId(), password);
        return map;
    }

    public Map<String, Object> getCode(String email) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(email)) {
            map.put("emailMsg", "Email invalid");
            return map;
        }
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            map.put("emailMsg", "Email does not exist");
            return map;
        }
        String code = CommunityUtils.generateUUID().substring(0, 4);
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        context.setVariable("code", code);
        String content = templateEngine.process("/mail/forget", context);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mailClient.sendMail(user.getEmail(), "Reset Password", content);
            }
        });
        thread.start();
        map.put("code", code);
        map.put("email", user.getEmail());
        return map;
    }
    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }
}
