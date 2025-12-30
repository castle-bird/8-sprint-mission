package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

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

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<UserDto> update(
            @PathVariable UUID id,
            @ModelAttribute UserUpdateRequest request,
            @RequestParam(required = false) MultipartFile profile
    ) {

        try {
            BinaryContentCreateRequest binaryContentCreateRequest = null;

            if (profile != null && !profile.isEmpty()) {
                binaryContentCreateRequest = new BinaryContentCreateRequest(
                        profile.getOriginalFilename(),
                        profile.getContentType(),
                        profile.getBytes() // 파일 읽는 도중 에러 날 수 있기에 try문 사용
                );
            }

            UserDto updatedUser = userService.update(id, request, binaryContentCreateRequest, profile);

            return ResponseEntity.ok(updatedUser);

        } catch (IOException e) {

            log.error("파일 처리 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @RequestMapping(value = "/online", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserStatus>> onlineStatus() {
        List<UserStatus> us = userStatusService.findAll();

        return ResponseEntity.ok(us);
    }

    @RequestMapping(value = "/online/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<UserStatus> updateOnlineStatus(
            @PathVariable UUID id
    ) {
        UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(Instant.now());

        UserStatus us = userStatusService.update(id, userStatusUpdateRequest);

        return ResponseEntity.ok(us);
    }
}
