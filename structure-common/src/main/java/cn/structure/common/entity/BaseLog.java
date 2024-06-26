package cn.structure.common.entity;

import cn.structure.common.enums.LogEnums;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *     日志实体基类
 * </p>
 * @author chuck
 * @since 2020-12-26
 * @version 1.0.1
 */
@Getter
@Setter
@ToString
public class BaseLog {

    /**
     * 日志类型
     */
    private LogEnums type;

    /**
     * 目标方法
     */
    private String targetMethod;

    /**
     * 目标参数
     */
    private Object args;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 耗时
     */
    private Long timeDiff;

    /**
     * 将日志转换为JSON的字符串
     * @return java.lang.String
     */
    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
