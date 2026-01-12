package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Tag(
    name = "Message Controller",
    description = "Message 관련 Controller입니다."
)
@RequestMapping("/api/messages")
public interface MessageApi {

  @Operation(
      summary = "메세지 생성",
      description = "메세지를 생성하는 컨트롤러 입니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Message가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = Message.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Channel | Author with id {channelId | authorId} not found"))
      ),
  })
  ResponseEntity<Message> create(
      @Parameter(
          name = "messageCreateRequest",
          description = "Message 생성 정보",
          required = true,
          in = ParameterIn.DEFAULT,
          schema = @Schema(implementation = MessageCreateRequest.class)
      )
      MessageCreateRequest messageCreateRequest,
      @Parameter(
          name = "attachments",
          description = "이미지 첨부파일",
          in = ParameterIn.DEFAULT,
          array = @ArraySchema(schema = @Schema(type = "string", format = "binary"))
      )
      List<MultipartFile> attachments
  );

  @Operation(
      summary = "메세지 수정",
      description = "메세지를 수정하는 컨트롤러 입니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = Message.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found"))
      ),
  })
  ResponseEntity<Message> update(
      @Parameter(
          name = "messageId",
          description = "수정할 Message ID",
          required = true,
          in = ParameterIn.PATH,
          schema = @Schema(implementation = UUID.class)
      )
      UUID messageId,
      @Parameter(
          name = "request",
          description = "수정할 Message 내용",
          required = true,
          in = ParameterIn.DEFAULT,
          schema = @Schema(implementation = MessageUpdateRequest.class)
      )
      MessageUpdateRequest request
  );

  @Operation(
      summary = "메세지 제거",
      description = "메세지를 제거하는 컨트롤러 입니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "Message가 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found"))
      ),
  })
  ResponseEntity<Void> delete(
      @Parameter(
          name = "messageId",
          description = "삭제할 Message ID",
          required = true,
          in = ParameterIn.PATH,
          schema = @Schema(implementation = UUID.class)
      )
      UUID messageId
  );

  @Operation(summary = "메세지 조회", description = "각채널의 메세지를 조회하는 컨트롤러 입니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class)))
      )
  })
  ResponseEntity<List<Message>> findAllByChannelId(
      @Parameter(
          name = "channelId",
          description = "조회할 채널 ID",
          required = true,
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = UUID.class)
      )
      UUID channelId
  );
}
