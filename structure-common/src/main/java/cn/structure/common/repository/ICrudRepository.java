package cn.structure.common.repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 仓储接口
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/12/23 16:09
 */
public interface ICrudRepository<T,ID> extends IQueryRepository<T,ID> {

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
     */
    void removeById(ID id);

    /**
     * 查询
     *
     * @param id 主键¬
     * @return 实体
     */
    T findById(ID id);


    /**
     * 批量保存
     *
     * @param entities 实体列表
     * @return 保存后的实体列表
     */
    List<T> saveBatch(List<T> entities);

    /**
     * 批量删除
     *
     * @param ids 主键列表
     */
    void removeBatchByIds(List<ID> ids);

    /**
     * 根据ID列表查询
     *
     * @param ids 主键列表
     * @return 实体列表
     */
    List<T> listByIds(List<ID> ids);

    /**
     * 统计数量
     *
     * @param condition 条件
     * @return 数量
     */
    long count(T condition);

    /**
     * 判断是否存在
     *
     * @param condition 条件
     * @return 是否存在
     */
    boolean exists(T condition);

}
