package cn.structure.common.ddd;

import java.io.Serializable;

/**
 * <p>
 * 仓储接口
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/12/23 16:09
 */
public interface ICrudRepository<T> extends IQueryRepository<T>{


    /**
     * 保存
     *
     * @param entity 实体
     * @return 实体
     */
    T save(T entity);

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Serializable id);

    /**
     * 更新
     *
     * @param entity 实体
     * @return 是否成功
     */
    boolean updateById(T entity);


}
