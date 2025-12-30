package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/binarycontents")
@RequiredArgsConstructor
@Slf4j
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BinaryContent> findById(@PathVariable UUID id) {

        BinaryContent bc = binaryContentService.find(id);

        return ResponseEntity.ok().body(bc);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BinaryContent>> findAllByIdIn(@RequestParam List<UUID> ids) {

        log.info("ids: {}", ids);

        List<BinaryContent> bc = binaryContentService.findAllByIdIn(ids);

        return ResponseEntity.ok().body(bc);
    }
}
