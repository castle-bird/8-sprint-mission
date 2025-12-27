package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Message> create(
            @ModelAttribute MessageCreateRequest request,
            @RequestParam(required = false) List<MultipartFile> attachments
    ) {

        try {
            List<BinaryContentCreateRequest> binaryContentCreateRequest = new ArrayList<>();

            if (attachments != null && !attachments.isEmpty()) {
                for (MultipartFile file : attachments) {
                    binaryContentCreateRequest.add(
                            new BinaryContentCreateRequest(
                                    file.getOriginalFilename(),
                                    file.getContentType(),
                                    file.getBytes()));
                }

                binaryContentService.saveMultiFiles(attachments);
            }

            Message message = messageService.create(request, binaryContentCreateRequest);

            return ResponseEntity
                    .ok()
                    .body(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<Message> update(
            @PathVariable UUID id,
            @ModelAttribute MessageUpdateRequest request
    ) {

        Message message = messageService.update(id, request);

        return ResponseEntity
                .ok()
                .body(message);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> delete(
            @PathVariable UUID id
    ) {

        messageService.delete(id);

        return ResponseEntity
                .ok()
                .body("삭제 완료");
    }

    @RequestMapping(value = "/channel/{chId}")
    @ResponseBody
    public ResponseEntity<List<Message>> findAllByChannelId(
            @PathVariable UUID chId
    ) {
        List<Message> messages = messageService.findAllByChannelId(chId);

        return ResponseEntity.ok().body(messages);
    }
}
