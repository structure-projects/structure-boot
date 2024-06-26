package cn.structured.ribbon.configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务列表
 *
 * @author cqliut
 * @version 2023.0221
 * @since 1.0.1
 */
@Slf4j
public class StructureStaticServerList extends AbstractServerList<Server> implements ServerList<Server> {

    private final List<Server> servers;

    private IClientConfig iClientConfig;

    public StructureStaticServerList(Server... servers) {
        this.servers = Arrays.asList(servers);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        this.iClientConfig = iClientConfig;
    }

    @Override
    public List<Server> getInitialListOfServers() {
        return this.getServers();
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        return this.getServers();
    }

    private List<Server> getServers() {
        String clientName = iClientConfig.getClientName();
        log.debug(clientName);
        return this.servers.stream()
                .filter(server -> server.getMetaInfo().getAppName().equals(clientName))
                .collect(Collectors.toList());
    }
}
