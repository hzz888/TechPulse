package com.elec5619.community.util;

import com.elec5619.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author Zhengzuo Huo
 */
@Component
public class HostHolder {

        private ThreadLocal<User> users = new ThreadLocal<>();

        public void setUser(User user) {
            users.set(user);
        }

        public User getUser() {
            return users.get();
        }

        public void clear() {
            users.remove();
        }

}
