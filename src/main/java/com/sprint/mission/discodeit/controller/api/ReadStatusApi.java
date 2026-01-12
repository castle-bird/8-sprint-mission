package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

@Tag(name = "ReadStatus Controller", description = "User의 각체널 메세지 읽은 시간을 저장하는 Controller입니다.")
@RequestMapping("/api/readStatuses")
public interface ReadStatusApi {

  @Operation(
      summary = "사용자의 메세지 읽음 상태 생성",
      description = "사용자가 채팅방의 글을 마지막으로 읽은 시간을 저장하는 컨트롤러입니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ReadStatus.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Channel | User with id {channelId | userId} not found"))
      ),
      @ApiResponse(
          responseCode = "400", description = "이미 읽음 상태가 존재함",
          content = @Content(examples = @ExampleObject(value = "ReadStatus with userId {userId} and channelId {channelId} already exists"))
      )
  })
  ResponseEntity<ReadStatus> create(
      @Parameter(
          name = "request",
          description = "읽은 시간 생성",
          required = true,
          schema = @Schema(implementation = ReadStatusCreateRequest.class)
      )
      ReadStatusCreateRequest request
  );

  @Operation(
      summary = "사용자의 메세지 읽음 상태 수정",
      description = "사용자가 채팅방의 글을 마지막으로 읽은 시간을 업데이트하는 컨트롤러입니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ReadStatus.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "ReadStatus with id {readStatusId} not found"))
      )
  })
  ResponseEntity<ReadStatus> update(
      @Parameter(
          name = "readStatusId",
          description = "읽음 상태 ID",
          required = true,
          in = ParameterIn.PATH,
          schema = @Schema(implementation = UUID.class)
      )
      UUID readStatusId,
      @Parameter(
          name = "request",
          description = "읽은 가장 최근 시간",
          required = true,
          schema = @Schema(implementation = ReadStatusUpdateRequest.class)
      )
      ReadStatusUpdateRequest request
  );

  @Operation(
      summary = "특정 사용자가 접속 가능한 채널 모두 찾기",
      description = "특정 사용자가 메세지 읽음 상태를 이용해 입장 가능한 채널의 목록을 나타냅니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message 읽음 상태 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatus.class)))
      )
  })
  ResponseEntity<List<ReadStatus>> findAllByUserId(
      @Parameter(
          name = "userId",
          description = "접속 가능한 채널을 찾을 사용자 ID",
          required = true,
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = UUID.class)
      )
      UUID userId
  );
}
