package cn.structure.starter.log.aop;

import cn.structure.common.entity.FunctionLog;
import cn.structure.common.enums.LogEnums;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 日志记录切面
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021-01-01
 */
@Aspect
@Component
public class ParamLogsAspect {

    private Logger log = LoggerFactory.getLogger(ParamLogsAspect.class);

    @Pointcut(value = "@annotation(cn.structure.starter.log.anno.AspectParamLog)")
    public void aroundPointcut() {
    }

    /**
     * <p>
     * 日志入参记录
     * </p>
     **/
    @Around("aroundPointcut()")
    public Object recodInParam(ProceedingJoinPoint pjp) throws Throwable {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String targetClass = pjp.getTarget().getClass().toString();
        String targetMethod = pjp.getSignature().getName();
        //传入参数
        Object[] args = pjp.getArgs();
        //回参
        Object outParam = null;
        Date beginDate = new Date();
        FunctionLog functionLog = new FunctionLog();
        functionLog.setType(LogEnums.FUNCTION);
        functionLog.setBeginTime(sdf.format(beginDate));
        functionLog.setTargetMethod(targetClass + "." + targetMethod);
        functionLog.setArgs(args);
        try {
            outParam = pjp.proceed();
        } catch (Throwable throwable) {
            Throwable cause = throwable.getCause();
            String message = cause.getMessage();
            log.error(message);
            throw throwable;
        }
        Date endDateTime = new Date();
        functionLog.setEndTime(sdf.format(endDateTime));
        functionLog.setTimeDiff(endDateTime.getTime() - beginDate.getTime());
        log.info(functionLog.toJSONString());
        return outParam;
    }
}
