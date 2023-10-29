package com.elec5619.community.controller;

import com.elec5619.community.entity.User;
import com.elec5619.community.service.UserService;
import com.elec5619.community.util.CommunityConstant;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Zhengzuo Huo
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "Register successfully, we have sent you an activation email, please check your email box.");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";

        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == CommunityConstant.ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "Activation successful.");
            model.addAttribute("target", "/login");
        } else if (result == CommunityConstant.ACTIVATION_REPEAT) {
            model.addAttribute("msg", "Your account has already been activated.");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "Activation failed.");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        session.setAttribute("kaptcha", text);
        response.setContentType("image/png");
        try (OutputStream os = response.getOutputStream();) {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("Failed to get kaptcha: " + e.getMessage());
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme, Model model, HttpSession session, HttpServletResponse response) {
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "Verification code incorrect");
            return "/site/login";
        }

        int expiredSeconds = rememberme ? CommunityConstant.REMEMBER_EXPIRED_SECONDS : CommunityConstant.DEFAULT_EXPIRED_SECONDS;

        Map<String, Object> map = userService.login(username, password, expiredSeconds);

        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }

    @RequestMapping(path = "/forget", method = RequestMethod.GET)
    public String getForgetPage() {
        return "/site/forget";
    }

    @RequestMapping(path = "/forget", method = RequestMethod.POST)
    public String forget(String email, String code, String password, String confirmPassword, HttpSession session, Model model) {

        if (StringUtils.isBlank(email)) {
            model.addAttribute("emailMsg", "Email cannot be empty");
            return "/site/forget";
        }

        if (StringUtils.isBlank(password)) {
            model.addAttribute("passwordMsg", "Password cannot be empty");
            return "/site/forget";
        }

        String verificationEmail = (String) session.getAttribute("email");

        String verificationCode = (String) session.getAttribute("code");

        if (StringUtils.isBlank(verificationEmail) || !verificationEmail.equals(email)) {
            model.addAttribute("emailMsg", "Invalid email or no verification code sent");
            return "/site/forget";
        }

        if (StringUtils.isBlank(verificationCode) || StringUtils.isBlank(code) || !code.equalsIgnoreCase(verificationCode)) {
            model.addAttribute("codeMsg", "Wrong verification code");
            return "/site/forget";
        }

        Map<String, Object> map = userService.resetPassword(email, password, confirmPassword);

        if(map == null || map.isEmpty()){
            model.addAttribute("msg", "Reset password successfully.");
            model.addAttribute("target", "/login");
            return "/site/operate-result";
        } else {
            model.addAttribute("emailMsg", map.get("emailMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("confirmPasswordMsg", map.get("confirmPasswordMsg"));
            return "/site/forget";
        }

    }

    @RequestMapping(path = "/code", method = RequestMethod.GET)
    public String getCode(@RequestParam("email") String email, HttpSession session, Model model) {
        if (StringUtils.isBlank(email)) {
            model.addAttribute("emailMsg", "Email cannot be empty");
            return "/site/forget";
        }
        Map<String, Object> map = userService.getCode(email);
        if (map.containsKey("code") && map.containsKey("email")) {
            session.setAttribute("code", map.get("code"));
            session.setAttribute("email", map.get("email"));
        } else {
            model.addAttribute("emailMsg", map.get("emailMsg"));
        }
        return "/site/forget";
    }
}

