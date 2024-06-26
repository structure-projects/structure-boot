package cn.structured.mybatis.plus.starter.base;

import cn.structured.mybatis.plus.starter.core.QueryJoinPageListWrapper;
import cn.structured.mybatis.plus.starter.vo.ReqPage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 * service基类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/2 14:14
 */
public interface IBaseService<T> extends IService<T> {


    /**
     * 分页查询
     *
     * @param page         分页查询
     * @param queryWrapper 查询条件
     * @return 返回用户分页信息
     */
    IPage<T> page(IPage<T> page, QueryJoinPageListWrapper<T> queryWrapper);


    /**
     * 查询列表
     * @param queryWrapper 查询条件
     * @return
     */
    List<T> list(QueryJoinPageListWrapper<T> queryWrapper);

    /**
     * 分页查询
     *
     * @param reqPage 分页参数
     * @param isJoin  是否表关联
     * @return
     */
    IPage<T> page(ReqPage reqPage, boolean isJoin);

    /**
     * 分页查询
     *
     * @param reqPage 分页
     * @return {@link IPage}
     */
    default IPage<T> page(ReqPage reqPage) {
        return page(reqPage, true);
    }

}
