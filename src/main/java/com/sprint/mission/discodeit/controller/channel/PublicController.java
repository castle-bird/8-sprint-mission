package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/channels/public")
@RequiredArgsConstructor
@Slf4j
public class PublicController {

    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ChannelDto> create(@ModelAttribute PublicChannelCreateRequest request) {

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ChannelDto> findChannel(@PathVariable String id) {

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ChannelDto> findChannels() {

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<ChannelDto> update(@PathVariable String id) {

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<ChannelDto> delete(@PathVariable String id) {

        return ResponseEntity.ok().build();
    }
}
