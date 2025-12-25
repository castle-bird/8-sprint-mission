package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String create(@ModelAttribute UserCreateRequest request) {

        User user = userService.create(request, null);
        log.info("생성된 유저 이름: {}", user.getUsername());

        return "회원가입 완료";
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public String update(@ModelAttribute UserDto request) {

        if(!userService.existsByUsername(request.username())) {
            return "존재하지 않는 사용자 이름입니다.";
        }

        if (!userService.existsByEmail(request.email())) {
            return "존재하지 않는 사용자 이메일입니다.";
        }

        //log.info("수정된 유저 이름: {}", user.getUsername());

        return "수정 완료";
    }
}
