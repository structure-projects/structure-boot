package cn.structure.common.event;

import lombok.Data;

/**
 * <p>
 * 用户删除事件
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/12/23 16:09
 */
@Data
public class UserDeleteEvent implements Event {

    /**
     * 用户id
     */
    private String userId;
}
