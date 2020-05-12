package com.coin.msdict.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局事务配置
 * 2018-07-05
 * @author zhanglw
 */
@Aspect
@Configuration
public class TxAdviceInterceptorConfig {

    private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.coin.msdict.web.service..*.*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    /*TransactionDefinition.PROPAGATION_REQUIRED：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。
    TransactionDefinition.PROPAGATION_REQUIRES_NEW：创建一个新的事务，如果当前存在事务，则把当前事务挂起。
    TransactionDefinition.PROPAGATION_SUPPORTS：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
    TransactionDefinition.PROPAGATION_NOT_SUPPORTED：以非事务方式运行，如果当前存在事务，则把当前事务挂起。
    TransactionDefinition.PROPAGATION_NEVER：以非事务方式运行，如果当前存在事务，则抛出异常。
    TransactionDefinition.PROPAGATION_MANDATORY：如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
    TransactionDefinition.PROPAGATION_NESTED：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，
    则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED。
    尽量避免事务嵌套，如需事务嵌套的业务请注意事务的传播行为！
    */
    @Bean
    public TransactionInterceptor txAdvice() {

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        /*只读事务，不做更新操作*/
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

        /*当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务*/
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        /*创建一个新的事务，如果当前存在事务，则把当前事务挂起*/
        RuleBasedTransactionAttribute requiresNew = new RuleBasedTransactionAttribute();
        requiresNew.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiresNew.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);


//        requiredTx.setTimeout(TX_METHOD_TIMEOUT);
        Map<String, TransactionAttribute> txMap = new HashMap<>(20);

        txMap.put("add*", requiredTx);
        txMap.put("save*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("set*", requiredTx);
        txMap.put("del*", requiredTx);
        txMap.put("set*", requiredTx);
        //扣减库存相关方法
        txMap.put("substract*",requiredTx);

        // 异常记录插入及删除操作，使用独立事务
        txMap.put("saveRecord", requiresNew);


        txMap.put("get*", readOnlyTx);
        txMap.put("find*", readOnlyTx);
        txMap.put("query*", readOnlyTx);
        txMap.put("list*", readOnlyTx);
        txMap.put("select*", readOnlyTx);
        txMap.put("*", readOnlyTx);
        source.setNameMap(txMap);
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        return txAdvice;
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}
