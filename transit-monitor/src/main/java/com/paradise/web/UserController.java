package com.paradise.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Paradise
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/info")
    public R info(String token) {
        log.info(token);
        Map<String, Object> map = new HashMap<>();
        map.put("roles", new String[]{"admin"});
        map.put("introduction", "I am a super administrator");
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name", "Super Admin");
        return R.success(map);
    }

    @RequestMapping("/login")
    public R login(String username, String password) {
        log.info("username: " + username);
        log.info("password: " + password);
        Map<String, String> map = new HashMap<>();
        map.put("token", "admin-token");
        return R.success("login", map);
    }

    @RequestMapping("/logout")
    public R logout() {
        return R.success("login");
    }
}
