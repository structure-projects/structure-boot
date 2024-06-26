package cn.structured.mybatis.plus.starter.base;

import java.io.Serializable;

/**
 * 带启用停用的接口
 *
 * @author cqliut
 * @version 2023.0713
 * @since 1.0.1
 */
public interface IEnableService {


    /**
     * 启用
     *
     * @param id 主键ID
     */
    void enable(Serializable id);

    /**
     * 停用
     *
     * @param id 主键ID
     */
    void disable(Serializable id);

}
