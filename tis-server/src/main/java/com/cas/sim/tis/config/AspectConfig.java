package com.cas.sim.tis.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.thrift.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AspectConfig {
	@Pointcut("execution(public * com.cas.sim.tis.services.*.*(..))")
	public void servicePointcut() {
	}

	@Around("servicePointcut()")
	public Object onAround(ProceedingJoinPoint pjp) {
		String msg = String.format("执行 %s.%s(%s)", //
				pjp.getSignature().getDeclaringTypeName(), //
				pjp.getSignature().getName(), //
				getArgs(pjp.getArgs()));
		log.info(msg);

		Object result;
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			msg = String.format("执行 %s.%s(%s)出现异常%s", //
					pjp.getSignature().getDeclaringTypeName(), //
					pjp.getSignature().getName(), //
					getArgs(pjp.getArgs()), //
					e.getMessage());
			log.error(msg, e);
			result = ResponseEntity.failure(msg);
		}
		return result;
	}

	private String getArgs(Object[] objects) {
		return Arrays.asList(objects).toString();
	}
}
