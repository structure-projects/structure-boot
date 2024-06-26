package cn.structured.mybatisplus.generate.example.dao;

import cn.structured.mybatis.plus.starter.base.IBaseMapper;
import cn.structured.mybatisplus.generate.example.entity.Staff;

/**
 * <p>
 * 人员表 Mapper 接口
 * </p>
 *
 * @author chuck
 * @since 2021-07-30
 */
public interface StaffMapper extends IBaseMapper<Staff> {

//    @Results(value = {
//            @Result(column = "orgPost_id",property = "orgPost.id",jdbcType = JdbcType.INTEGER),
//            @Result(column = "orgPost_postName",property = "orgPost.postName",jdbcType = JdbcType.VARCHAR)
//    })
//    @Override
//    @SelectProvider(value = SelectJoinList.class ,method = "selectJoinList")
//    IPage<Staff> selectJoinPageList(@Param("page") IPage page, @Param("wrapper") QueryJoinPageListWrapper<Staff> wrapper);
}
