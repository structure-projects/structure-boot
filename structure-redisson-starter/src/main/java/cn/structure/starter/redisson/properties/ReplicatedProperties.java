package cn.structure.starter.redisson.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 云托管
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-23
 */
@Getter
@Setter
@ToString
public class ReplicatedProperties extends MultipleServerProperties {
    /**
     * 云托管的redis地址
     */
    private List<String> nodeAddresses = new ArrayList();
    /**
     * 节点变化扫描间隔时间
     */
    private int scanInterval = 1000;
}
