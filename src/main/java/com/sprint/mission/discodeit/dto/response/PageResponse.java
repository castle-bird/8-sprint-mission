package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    int number,
    int size,
    boolean hasNext,
    Long totalElements // Slice인 경우 null, Page인 경우 값 존재
) {

}
