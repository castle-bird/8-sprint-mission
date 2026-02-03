package com.sprint.mission.discodeit.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)    // 메서드에만 붙일 수 있는 어노테이션
@Retention(RetentionPolicy.RUNTIME)  // 런타임 시점까지 어노테이션 정보 유지
public @interface LogExecution {

  String action() default "메서드 기능을 적어주세요. e.g. Controller, Service";  // 어노테이션 속성 (기본값: 빈 문자열)

  String purpose() default "메서드의 목적을 적어주세요. e.g. 사용자 생성, 사용자 수정...";
}