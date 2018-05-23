package com.cas.sim.tis.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
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
	@Pointcut("execution(public * com.cas.sim.tis.action.*.*(..))")
	public void actionPointcut() {
	}

	@Around("actionPointcut()")
	public Object onAround(ProceedingJoinPoint pjp) {
		String msg = String.format("执行 %s.%s(%s)", //
				pjp.getSignature().getDeclaringTypeName(), //
				pjp.getSignature().getName(), //
				getArgs(pjp.getArgs()));
		log.info(msg);
		Object entity = null;
		try {
			entity = pjp.proceed();
		} catch (Throwable e) {
			msg = String.format("执行 %s.%s(%s)出现异常%s", //
					pjp.getSignature().getDeclaringTypeName(), //
					pjp.getSignature().getName(), //
					getArgs(pjp.getArgs()), //
					e.getMessage());
			log.error(msg, e);
		}
		return entity;
	}

	private String getArgs(Object[] objects) {
		return Arrays.asList(objects).toString();
	}

	@Pointcut("execution(public * com.cas.sim.tis.services.*.*(..))")
	public void servicePointcut() {
	}

	@AfterReturning(returning = "ret", pointcut = "servicePointcut()")
	public Object onServiceReturn(Object ret) {
		if (ret instanceof ResponseEntity) {
			ResponseEntity resp = (ResponseEntity) ret;
			if (resp.code == 500) {
				throw new RuntimeException(resp.msg);
			}
		}
		return ret;
	}

}
