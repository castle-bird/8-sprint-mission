package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Tag(name = "Channel Controller", description = "Channel 관련 Controller입니다.")
@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  @Operation(summary = "공개채널 생성", description = "공개채널 생성하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = Channel.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  @PostMapping("/public")
  public ResponseEntity<Channel> create(@RequestBody PublicChannelCreateRequest request) {
    Channel createdChannel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
  }

  @Operation(summary = "비공개채널 생성", description = "비공개채널 생성하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = Channel.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  @PostMapping("/private")
  public ResponseEntity<Channel> create(@RequestBody PrivateChannelCreateRequest request) {
    Channel createdChannel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
  }

  @Operation(summary = "채널 수정", description = "채널을 수정하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = Channel.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  @PatchMapping("/{channelId}")
  public ResponseEntity<Channel> update(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel udpatedChannel = channelService.update(channelId, request);
    return ResponseEntity.status(HttpStatus.OK).body(udpatedChannel);
  }

  @Operation(summary = "채널 제거", description = "채널을 제거하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "제거 성공", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "채널 조회", description = "특정 사용자가 접속 가능한 채널을 모두 조회하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  @GetMapping()
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(channels);
  }
}
