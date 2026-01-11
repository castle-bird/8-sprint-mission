package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Channel Controller", description = "Channel 관련 Controller입니다.")
@RequestMapping("/api/channels")
public interface ChannelApi {

  @Operation(summary = "공개채널 생성", description = "공개채널 생성하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = Channel.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<Channel> create(PublicChannelCreateRequest request);

  @Operation(summary = "비공개채널 생성", description = "비공개채널 생성하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = Channel.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<Channel> create(PrivateChannelCreateRequest request);

  @Operation(summary = "채널 수정", description = "채널을 수정하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = Channel.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<Channel> update(UUID channelId, PublicChannelUpdateRequest request);

  @Operation(summary = "채널 제거", description = "채널을 제거하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "제거 성공", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<Void> delete(UUID channelId);

  @Operation(summary = "채널 조회", description = "특정 사용자가 접속 가능한 채널을 모두 조회하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<List<ChannelDto>> findAll(UUID userId);
}
