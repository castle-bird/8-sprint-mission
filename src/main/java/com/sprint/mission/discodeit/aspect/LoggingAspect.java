package com.sprint.mission.discodeit.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Around("@annotation(logExecution)")  // @LogExecution 붙은 모든 메서드
  public Object logExecution(
      ProceedingJoinPoint joinPoint, // AOP가 적용되는 메서드의 모든 정보
      LogExecution logExecution // 사용 어노테이션 속성값들
  ) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    String action = logExecution.action();
    String purpose = logExecution.purpose();
    String args = Arrays.toString(joinPoint.getArgs());

    // 실행 전 INFO 로그
    log.info("[{}] {}#{} 시작 - 매개변수: {} / 목적: {}",
        action,
        joinPoint.getTarget().getClass().getSimpleName(), // AOP 대상이 되는 실제 클래스 이름
        methodName,
        args,
        purpose
    );

    long start = System.currentTimeMillis(); // 메서드 시작 시간
    try {
      Object result = joinPoint.proceed();
      long duration = System.currentTimeMillis() - start; // 메서드 작동 시간

      // 성공 시 INFO 로그
      log.info("[{}] {}#{} 완료 - 소요시간: {}ms, 결과: {}",
          action,
          joinPoint.getTarget().getClass().getSimpleName(),
          methodName, duration, result
      );
      return result;
    } catch (Exception e) {
      // 예외 시 ERROR 로그
      log.error("[{}] {}#{} 실패 - 에러: {}",
          action.isEmpty() ? "SERVICE" : action,
          joinPoint.getTarget().getClass().getSimpleName(),
          methodName, e.getMessage(), e);
      throw e;
    }
  }
}