package cn.structured.mybatis.plus.starter.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 入参分页
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/30 14:00
 */
@Data
public class ReqPage {

    private Integer currentPage;

    private Integer pageSize;

    private String keyword;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

}
