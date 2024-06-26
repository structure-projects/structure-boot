package cn.structured.mybatisplus.generate.example.entity;

import cn.structured.mybatis.plus.starter.annotations.FieldJoin;
import cn.structured.mybatis.plus.starter.annotations.Join;
import cn.structured.mybatis.plus.starter.annotations.JoinCondition;
import cn.structured.mybatis.plus.starter.annotations.LogicDelete;
import cn.structured.mybatis.plus.starter.enums.JoinResultEnum;
import cn.structured.mybatis.plus.starter.enums.JoinTypeEnum;
import cn.structured.mybatisplus.generate.example.group.PostGroup;
import cn.structured.mybatisplus.generate.example.group.StaffPostGroup;
import cn.structured.mybatisplus.generate.example.group.UserGroup;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 人员表
 * </p>
 *
 * @author chuck
 * @since 2021-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("staff")
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("id")
    private Integer id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 性别,N 未知,M 男 ,F 女
     */
    @TableField("sex")
    private String sex;

    /**
     * 出生日期
     */
    @TableField("birth_date")
    private LocalDateTime birthDate;

    /**
     * 头像
     */
    @TableField("head_portrait")
    private String headPortrait;

    /**
     * 文化程度
     */
    @TableField("degree_of_education")
    private String degreeOfEducation;

    /**
     * 健康状况
     */
    @TableField("health")
    private String health;

    /**
     * 婚姻状况
     */
    @TableField("marriage")
    private String marriage;

    /**
     * 民族
     */
    @TableField("nation")
    private String nation;

    /**
     * 政治面貌
     */
    @TableField("politics")
    private String politics;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 户口住址
     */
    @TableField("account_address")
    private String accountAddress;

    /**
     * 居住地址
     */
    @TableField("residence_address")
    private String residenceAddress;

    /**
     * 紧急联系人
     */
    @TableField("emergency_contact")
    private String emergencyContact;

    /**
     * 紧急联系人电话
     */
    @TableField("emergency_contact_phone")
    private String emergencyContactPhone;

    /**
     * 是否删除:0 否,1是
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean del;

    /**
     * 创建人
     */
    @TableField(value = "create_id", fill = FieldFill.INSERT)
    private Integer createId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 最后修改人
     */
    @TableField(value = "update_id", fill = FieldFill.INSERT_UPDATE)
    private Integer updateId;

    /**
     * 最后修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "name", exist = false)
    @FieldJoin(value = {
            @Join(group = {UserGroup.class, PostGroup.class}, joinTarget = Staff.class, aliasName = "ss", columns = "name", value = {
                    @JoinCondition(currentColumn = "create_id", targetColumn = "id"),
            })
    })
    private String createBy;

    /**
     * 职务列表
     */
    @TableField(exist = false)
    @FieldJoin(type = JoinResultEnum.MANY, result = OrgPost.class, value = {
            @Join(group = {PostGroup.class, StaffPostGroup.class}, joinType = JoinTypeEnum.LEFT_JOIN, joinTarget = StaffPost.class, aliasName = "sp", value = {
                    @JoinCondition(currentColumn = "id", targetColumn = "staff_id")
            }),
            @Join(group = {StaffPostGroup.class}, result = true, joinType = JoinTypeEnum.LEFT_JOIN, joinTarget = OrgPost.class, aliasName = "org", columns = {"id", "post_name"}, value = {
                    @JoinCondition(condition = "sp.post_id = org.id")
            })
    })
    private List<OrgPost> orgPost;

    /**
     * 修改人
     */
    @TableField(exist = false)
    @FieldJoin(value = {
            @Join(group = {UserGroup.class}, joinTarget = Staff.class, aliasName = "us", columns = {"id", "name"}, value = {
                    @JoinCondition(currentColumn = "update_id", targetColumn = "id"),
            })
    })
    private Staff updateBy;

}
