package com.elec5619.community.util;

/**
 * Constants for community project.
 * @author Zhengzuo Huo
 */
public interface CommunityConstant {

    /**
     * delete
     */
    String TOPIC_DELETE = "delete";

    int ACTIVATION_SUCCESS = 0;
    int ACTIVATION_REPEAT = 1;
    int ACTIVATION_FAILURE = 2;

    int DEFAULT_EXPIRED_SECONDS = 3600 * 24;

    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 60;

    int ENTITY_TYPE_POST = 1;

    int ENTITY_TYPE_COMMENT = 2;
    int ENTITY_TYPE_USER = 3;
    /**
     * authority:user
     */
    public String AUTHORITY_USER = "user";
    /**
     * authority:admin
     */
    public String AUTHORITY_ADMIN = "admin";
    /**
     * authority:blogger
     */
    public String AUTHORITY_MODERATOR = "moderator";

}
