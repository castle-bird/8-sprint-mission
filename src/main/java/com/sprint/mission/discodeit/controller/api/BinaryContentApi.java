package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
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

@Tag(name = "BinaryContent Controller", description = "BinaryContent 관련 컨트롤러입니다.")
@RequestMapping("/api/binaryContents")
public interface BinaryContentApi {

  @Operation(
      summary = "BinaryContent 1개 조회",
      description = "BinaryContent를 1개 조회할 수 있습니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "첨부 파일 조회 성공",
          content = @Content(schema = @Schema(implementation = BinaryContent.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "첨부 파일을 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "BinaryContent with id {binaryContentId} not found"))
      )
  })
  ResponseEntity<BinaryContentDto> find(
      @Parameter(
          name = "binaryContentId",
          description = "파일 ID",
          required = true,
          in = ParameterIn.PATH,
          schema = @Schema(implementation = UUID.class)
      )
      UUID binaryContentId
  );

  @Operation(summary = "BinaryContent 여러개 조회", description = "BinaryContent를 여러개 조회할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "첨부 파일 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = BinaryContent.class)))
      )
  })
  ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @Parameter(
          name = "binaryContentIds",
          description = "파일 ID 목록 list",
          required = true,
          in = ParameterIn.QUERY,
          array = @ArraySchema(schema = @Schema(implementation = UUID.class))
      )
      List<UUID> binaryContentIds
  );

  @Operation(summary = "프로필 이미지 다운로드", description = "프로필 이미지를 다운로드할 수 있다")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "다운로드 성공"
          //content = @Content(schema = @Schema(implementation = .class))
      )
  })
  ResponseEntity<?> download(
      @Parameter(
          name = "binaryContentId",
          description = "다운로드할 파일 아이디",
          required = true,
          in = ParameterIn.PATH,
          schema = @Schema(implementation = UUID.class)
      )
      UUID binaryContentId
  );
}
