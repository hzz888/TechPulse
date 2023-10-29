# TechPulse Community
ELEC5619 Group Project 


**Build:**  
Run MySQL and Redis, modify the `application.properties` config file, init the MySQL database using SQL files located at `database_init` and you're good to build this project with `mvn compile exec:java -Dexec.mainClass="com.elec5619.community.CommunityApplication"`

**Libraries:**

Spring 5.3.30

SpringBoot 2.7.16

MySQL 8.0

MyBatis 3.5.1

Redis 7.2

Thymeleaf 3.0.15

OkHttp 3.0

junit-jupyter 4.5.1

kaptcha 2.3.2

fastjson 1.2.78

jedis 5.0.2

Apache Commons Lang 3.13.0



**Working functionalities:**

Sign Up 

Our users can sign up easily with an email, and we have the mechanism to verify the email belongs to the current user.

Sign In

After signing up and verification of the email address, our users will be able to sign in to use this platform. We also deployed reCAPTCHA to prevent DDOS and authentication brute force.

Exit

When done using this platform, users can log out to keep accounts safe.

Change password

Users can change their passwords easily when they have logged in.

Reset password

If the user forgets the password, it can be reseted after email verification.

Change avatar

Users are able to alternate the default avatar with an uploaded picture.

Publish articles

Users are able to publish articles after login.

Search

Users are able to search articles whose title, content or comments contain search strings.

Comment

Users can leave comments on posted articles.

Upvote

Users can upvote others’ articles.

Send message

Users can send private messages to other users to chat with them.

Follow

Users can follow other users.

Statistics

This site can maintain statistics to be shown to admins.