package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final UserService userService;
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "user-list";
    }

    @RequestMapping(value = "/api/user/findAll", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserDto>> findAll() {

        List<UserDto> users = userService.findAll();

        return ResponseEntity.ok().body(users);
    }

    @RequestMapping(value = "/api/binaryContent/find", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BinaryContent> findProfile(@RequestParam UUID binaryContentId) {

        BinaryContent bc = binaryContentService.find(binaryContentId);

        return ResponseEntity.ok().body(bc);
    }
}
