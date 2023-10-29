package com.elec5619.community.dao;

import org.apache.ibatis.annotations.Mapper;
import com.elec5619.community.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author Zhengzuo Huo
 */
@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
