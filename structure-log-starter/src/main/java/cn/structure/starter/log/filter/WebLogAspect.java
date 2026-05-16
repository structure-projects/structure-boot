/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.structure.starter.log.filter;

import cn.structure.common.entity.ControllerLog;
import cn.structure.common.enums.LogEnums;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * webLogController的配置
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/6/3 12:05
 */
@Slf4j
public class WebLogAspect {

    private long c;

    private ControllerLog controllerLog;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public void doBefore(JoinPoint joinPoint) {
        log.debug("[WebLogAspect] 请求开始");
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("[WebLogAspect] 无法获取请求上下文");
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        controllerLog = new ControllerLog();
        controllerLog.setMethod(request.getMethod());
        controllerLog.setIpAddress(request.getRemoteAddr());
        controllerLog.setUrl(request.getRequestURL().toString());
        controllerLog.setTargetMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        controllerLog.setArgs(Arrays.toString(joinPoint.getArgs()));
        controllerLog.setBeginTime(sdf.format(new Date()));
        log.debug("[WebLogAspect] 请求信息 - method: {}, url: {}, ip: {}, target: {}", 
            controllerLog.getMethod(), controllerLog.getUrl(), controllerLog.getIpAddress(), controllerLog.getTargetMethod());
    }

    /**
     * 后处理  returning的值和doAfterReturning的参数名一致
     */
    public void doAfterReturning(Object ret) {
        controllerLog.setTimeDiff(c);
        controllerLog.setType(LogEnums.CONTROLLER);
        controllerLog.setEndTime(sdf.format(new Date()));
        // 处理完请求，返回内容
        log.info("[WebLogAspect] {}", JSON.toJSONString(controllerLog));
    }

    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        // ob 为方法的返回值
        Object ob = pjp.proceed();
        c = System.currentTimeMillis() - startTime;
        return ob;
    }

}
