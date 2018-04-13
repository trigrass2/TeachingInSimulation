package com.cas.sim.tis.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectConfig {
	private Logger logger = LoggerFactory.getLogger(AspectConfig.class);

	@Pointcut("execution(public * com.cas.sim.tis.action.*.*(..))")
	public void actionPointcut() {
	}

	@Pointcut("execution(public * com.cas.sim.tis.service.*.*(..))")
	public void servicePointcut() {
	}

	@Around("actionPointcut()")
	public Object onAround(ProceedingJoinPoint pjp) {
		try {
			String msg = String.format("执行 %s.%s(%s)", //
					pjp.getSignature().getDeclaringTypeName(), //
					pjp.getSignature().getName(), //
					pjp.getArgs());
			logger.debug(msg);
			return pjp.proceed();
		} catch (Throwable e) {
			String msg = String.format("执行 %s.%s(%s)出现异常%s", //
					pjp.getSignature().getDeclaringTypeName(), //
					pjp.getSignature().getName(), //
					pjp.getArgs(),//
					e.getMessage());
			logger.error(msg);
		}
		return null;
	}

	@AfterThrowing(pointcut = "servicePointcut()", throwing = "e")
	public void onException(Exception e) {
		logger.error(e.getMessage());
	}

//	@Before("actionPointcut()")
//	public void doBefore() throws Throwable {
//		// 接收到请求，记录请求内容
//		// 记录下请求内容
//		logger.info("doBefore");
//	}

//	@AfterReturning(pointcut = "actionPointcut()")
//	public void doAfterReturning() throws Throwable {
//		// 处理完请求，返回内容
//		logger.info("RESPONSE : ");
//	}
}
