package com.cas.sim.tis.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TxConfig {
	/* 事务拦截类型 */
	@Bean("txSource")
	public TransactionAttributeSource transactionAttributeSource() {
		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		/* 只读事务，不做更新操作 */
		RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
		readOnlyTx.setReadOnly(true);
		readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
		/* 当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务 */
		// RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
		// requiredTx.setRollbackRules(
		// Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		// requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED, Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		requiredTx.setTimeout(5);
		Map<String, TransactionAttribute> txMap = new HashMap<>();
		txMap.put("add*", requiredTx);
		txMap.put("save*", requiredTx);
		txMap.put("insert*", requiredTx);
		txMap.put("update*", requiredTx);
		txMap.put("delete*", requiredTx);
		txMap.put("get*", readOnlyTx);
		txMap.put("query*", readOnlyTx);
		source.setNameMap(txMap);
		return source;
	}

	/** 切面拦截规则 参数会自动从容器中注入 */
	@Bean
	public AspectJExpressionPointcutAdvisor pointcutAdvisor(TransactionInterceptor txInterceptor) {
		AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();
		pointcutAdvisor.setAdvice(txInterceptor);
		pointcutAdvisor.setExpression("execution (* com.cas.sim.tis.services.*.impl.*.*(..))");
		return pointcutAdvisor;
	}

	/* 事务拦截器 */
	@Bean("txInterceptor")
	TransactionInterceptor getTransactionInterceptor(PlatformTransactionManager tx) {
		return new TransactionInterceptor(tx, transactionAttributeSource());
	}
}
