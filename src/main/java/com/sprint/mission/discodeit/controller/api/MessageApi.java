package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
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
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message Controller", description = "Message 관련 Controller입니다.")
@RequestMapping("/api/messages")
public interface MessageApi {

  @Operation(summary = "메세지 생성", description = "메세지를 생성하는 컨트롤러 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = Message.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<Message> create(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments);

  @Operation(summary = "메세지 수정", description = "메세지를 수정하는 컨트롤러 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = Message.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<Message> update(UUID messageId, MessageUpdateRequest request);

  @Operation(summary = "메세지 제거", description = "메세지를 제거하는 컨트롤러 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "제거 성공", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<Void> delete(UUID messageId);

  @Operation(summary = "메세지 조회", description = "각채널의 메세지를 조회하는 컨트롤러 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class)))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<List<Message>> findAllByChannelId(UUID channelId);
}
