package cn.structure.common.repository;

import org.springframework.data.repository.Repository;

/**
 * <p>
 * 仓储接口
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/12/23 16:09
 */
public interface ICrudRepository<T, ID>{

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
    void deleteById(ID id);

    /**
     * 查询
     *
     * @param id 主键
     * @return 实体
     */
    T findById(ID id);

}
