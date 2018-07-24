package com.cas.sim.tis.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
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

	@AfterThrowing(throwing = "ex", pointcut = "actionPointcut()")
	public void afterThrowing(Throwable ex) {
		log.error("调用Action中的方法出现了异常", ex);
	}

	@Around("actionPointcut()")
	public Object onAround(ProceedingJoinPoint pjp) throws Throwable {
		String msg = String.format("%s.%s(%s)", //
				pjp.getSignature().getDeclaringType(), //
				pjp.getSignature().getName(), //
				getArgs(pjp.getArgs()));
		log.info(msg);
		return pjp.proceed();
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
