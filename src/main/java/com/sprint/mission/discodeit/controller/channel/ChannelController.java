package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PipedReader;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/channels")
@RequiredArgsConstructor
@Slf4j
public class ChannelController {

    private final ChannelService channelService;
    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Channel> createPublic(@ModelAttribute PublicChannelCreateRequest request) {

        Channel channel = channelService.create(request);

        return ResponseEntity
                .ok()
                .body(channel);
    }

    @RequestMapping(value = "/public/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ChannelDto>> findAllByUserId(@PathVariable UUID id) {
        List<ChannelDto> channels = channelService.findAllByUserId(id);

        return ResponseEntity
                .ok()
                .body(channels);
    }

    @RequestMapping(value = "/public/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<Channel> update(
            @PathVariable UUID id,
            @ModelAttribute PublicChannelUpdateRequest request
    ) {
        Channel updatedChannel = channelService.update(id, request);

        return ResponseEntity
                .ok()
                .body(updatedChannel);
    }

    @RequestMapping(value = "/public/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable UUID id) {

        channelService.delete(id);

        return ResponseEntity
                .ok()
                .body("삭제완료");
    }

    @RequestMapping(value = "/read/{channelId}/{userId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ReadStatus> readStatus(
            @PathVariable UUID channelId,
            @PathVariable UUID userId) {

        ReadStatusCreateRequest request = new ReadStatusCreateRequest(userId, channelId, Instant.now());

        ReadStatus readStatus = readStatusService.create(request);

        return ResponseEntity.ok().body(readStatus);
    }

    @RequestMapping(value = "/read/{readId}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<ReadStatus> readStatusUpdate(
            @PathVariable UUID readId) {

        ReadStatusUpdateRequest readStatusUpdateRequest = new ReadStatusUpdateRequest(Instant.now());

        ReadStatus readStatus = readStatusService.update(readId, readStatusUpdateRequest);

        return ResponseEntity
                .ok()
                .body(readStatus);
    }

    // ============== 비공개 채널 ==============
    @RequestMapping(value = "/private", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Channel> createPrivate(@ModelAttribute PrivateChannelCreateRequest request) {

        Channel channel = channelService.create(request);

        return ResponseEntity
                .ok()
                .body(channel);
    }


    @RequestMapping(value = "/all/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ChannelDto>> createPrivate(
            @PathVariable UUID id
    ) {

        List<ChannelDto> channels = channelService.findAllByUserId(id);

        return ResponseEntity
                .ok()
                .body(channels);
    }
}
