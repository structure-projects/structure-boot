package cn.structured.mybatis.plus.starter.core;

import cn.structured.mybatis.plus.starter.enums.JoinResultEnum;
import cn.structured.mybatis.plus.starter.enums.JoinTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 字段联表信息
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/5 14:33
 */
@Data
public class FieldJoinInfo {

    /**
     * 联表类型
     *
     * @return
     */
    private JoinTypeEnum joinType;

    /**
     * 联表返回的类型 如果是集合则表示泛型的值
     *
     * @return
     */
    private Class<?> result;

    /**
     * 联表结果类型
     *
     * @return
     */
    private JoinResultEnum type;

    /**
     * 要链的表
     *
     * @return
     */
    private List<JoinInfo> joinInfoList;

    /**
     * 实体属性名
     */
    private String resultName;

}
