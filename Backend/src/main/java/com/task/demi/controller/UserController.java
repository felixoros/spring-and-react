package com.task.demi.controller;

import com.task.demi.service.LiveUsersService;
import com.task.demi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final LiveUsersService liveUsersService;

    public UserController(
            UserService userService,
            LiveUsersService liveUsersService) {
        this.userService = userService;
        this.liveUsersService = liveUsersService;
    }

    @GetMapping(value = "/live")
    public ResponseEntity getLiveUsers() {
        try{
            return ResponseEntity.ok(this.liveUsersService.GetUsersFromLiveApi(false));
        } catch(Exception e) {
            // Logging + different catch clauses
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error \n");
        }
    }

    @GetMapping
    public ResponseEntity getUsers() {
        try{
            return ResponseEntity.ok(this.userService.GetUsersByCountry());
        } catch(Exception e) {
            // Logging + different catch clauses
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error \n");
        }
    }
}
