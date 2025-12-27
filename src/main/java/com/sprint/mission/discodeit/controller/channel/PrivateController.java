package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/channels/private")
@RequiredArgsConstructor
@Slf4j
public class PrivateController {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ChannelDto> create() {

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
