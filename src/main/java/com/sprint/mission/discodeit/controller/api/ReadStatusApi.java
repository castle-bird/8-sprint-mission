package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

@Tag(name = "ReadStatus Controller", description = "User의 각체널 메세지 읽은 시간을 저장하는 Controller입니다.")
@RequestMapping("/api/readStatuses")
public interface ReadStatusApi {

  @Operation(summary = "사용자의 메세지 읽음 상태 생성", description = "사용자가 채팅방의 글을 마지막으로 읽은 시간을 저장하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = ReadStatus.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<ReadStatus> create(ReadStatusCreateRequest request);

  @Operation(summary = "사용자의 메세지 읽음 상태 수정", description = "사용자가 채팅방의 글을 마지막으로 읽은 시간을 업데이트하는 컨트롤러입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ReadStatus.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<ReadStatus> update(UUID readStatusId, ReadStatusUpdateRequest request);

  @Operation(summary = "특정 사용자가 접속 가능한 채널 모두 찾기", description = "특정 사용자가 메세지 읽음 상태를 이용해 입장 가능한 채널의 목록을 나타냅니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatus.class)))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<List<ReadStatus>> findAllByUserId(UUID userId);
}
