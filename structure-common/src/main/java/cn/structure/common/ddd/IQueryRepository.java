package cn.structure.common.ddd;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 仓储接口
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/12/23 16:09
 */
public interface IQueryRepository<T> {

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return 实体
     */
    T queryById(Serializable id);

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return 实体
     */
    Optional<T> queryByIdOptional(Serializable id);

    /**
     * 根据条件查询
     *
     * @param entity 查询条件
     * @return 实体
     */
    T queryOne(T entity);

    /**
     * 根据条件查询
     *
     * @param entity 查询条件
     * @return 实体
     */
    Optional<T> queryOneOptional(T entity);


    /**
     * 根据条件查询
     *
     * @param entity 精确查询条件
     * @return 列表
     */
    List<T> queryList(T entity);


    /**
     * 分页查询
     *
     * @param reqPage 分页参数
     * @return 分页结果
     */
    ResPage<T> queryPage(ReqPage reqPage);


}
