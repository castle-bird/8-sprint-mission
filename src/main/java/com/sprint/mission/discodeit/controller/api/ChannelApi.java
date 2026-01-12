package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
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

@Tag(name = "Channel Controller", description = "Channel 관련 Controller입니다.")
@RequestMapping("/api/channels")
public interface ChannelApi {

  @Operation(
      summary = "공개채널 생성",
      description = "공개채널 생성하는 컨트롤러입니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Public Channel이 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = Channel.class))
      )
  })
  ResponseEntity<Channel> create(
      @Parameter(
          name = "request",
          description = "공개채널 생성정보",
          required = true,
          schema = @Schema(implementation = PublicChannelCreateRequest.class)
      )
      PublicChannelCreateRequest request
  );

  @Operation(
      summary = "비공개채널 생성",
      description = "비공개채널 생성하는 컨트롤러입니다."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "Private Channel이 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = Channel.class))),
  })
  ResponseEntity<Channel> create(
      @Parameter(
          name = "request",
          description = "비공개채널 생성정보",
          required = true,
          schema = @Schema(implementation = PrivateChannelCreateRequest.class)
      )
      PrivateChannelCreateRequest request
  );

  @Operation(
      summary = "채널 수정",
      description = "채널을 수정하는 컨트롤러입니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Channel 정보가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = Channel.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found"))
      ),
      @ApiResponse(
          responseCode = "400", description = "Private Channel은 수정할 수 없음",
          content = @Content(examples = @ExampleObject(value = "Private channel cannot be updated"))
      )
  })
  ResponseEntity<Channel> update(
      @Parameter(
          name = "channelId",
          description = "채널 ID",
          required = true,
          in = ParameterIn.PATH,
          schema = @Schema(implementation = UUID.class)
      )
      UUID channelId,
      @Parameter(
          name = "request",
          description = "채널의 수정 내용들",
          required = true,
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = PublicChannelUpdateRequest.class)
      )
      PublicChannelUpdateRequest request
  );

  @Operation(
      summary = "채널 제거",
      description = "채널을 제거하는 컨트롤러입니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "Channel이 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found"))
      )
  })
  ResponseEntity<Void> delete(
      @Parameter(
          name = "channelId",
          description = "채널 ID",
          required = true,
          in = ParameterIn.PATH,
          schema = @Schema(implementation = UUID.class)
      )
      UUID channelId
  );

  @Operation(
      summary = "채널 조회",
      description = "특정 사용자가 접속 가능한 채널을 모두 조회하는 컨트롤러입니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Channel 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)))
      )
  })
  ResponseEntity<List<ChannelDto>> findAll(
      @Parameter(
          name = "userId",
          description = "사용자 ID",
          required = true,
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = UUID.class)
      )
      UUID userId
  );
}
