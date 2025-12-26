package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UserDto> create(@ModelAttribute UserCreateRequest request) {

        UserDto user = userService.create(request, null);
        log.info("생성된 유저 이름: {}", user.username());

        return ResponseEntity
                .ok()
                .body(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<UserDto> find(@PathVariable UUID id) {
        UserDto userDto = userService.find(id);
        log.info("조회된 유저 이름: {}", userDto.username());

        return ResponseEntity
                .ok()
                .body(userDto);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtoList = userService.findAll();
        log.info("조회된 유저 수: {}", userDtoList.size());

        return ResponseEntity
                .ok()
                .body(userDtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);

        return ResponseEntity
                .noContent() // 204: 성공, 반환값은 없음
                .build();
    }
}
