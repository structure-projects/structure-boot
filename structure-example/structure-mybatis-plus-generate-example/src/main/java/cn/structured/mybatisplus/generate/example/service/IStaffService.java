package cn.structured.mybatisplus.generate.example.service;

import cn.structured.mybatis.plus.starter.base.IBaseService;
import cn.structured.mybatis.plus.starter.vo.ReqPage;
import cn.structured.mybatisplus.generate.example.entity.Staff;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 人员表 服务类
 * </p>
 *
 * @author chuck
 * @since 2021-07-30
 */
public interface IStaffService extends IBaseService<Staff> {

    /**
     * 分页查询职工列表
     *
     * @param reqPageStaffVO
     * @return
     */
    @Override
    IPage<Staff> page(ReqPage reqPageStaffVO);

    /**
     * 通过用户ID查询详情
     * @param id
     * @return
     */
    Staff getStaffById(Integer id);

}
