package cn.structured.ribbon.configuration;

import cn.structured.ribbon.model.StructureServer;
import com.netflix.client.DefaultLoadBalancerRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * @author cqliut
 * @version 2023.0222
 * @since 1.0.1
 */
@Configuration
@EnableConfigurationProperties
public class StructureRibbonClientConfiguration {
    public static final int DEFAULT_CONNECT_TIMEOUT = 1000;
    public static final int DEFAULT_READ_TIMEOUT = 1000;
    public static final boolean DEFAULT_GZIP_PAYLOAD = true;

    @Resource
    private ServerProperties serverProperties;

    @RibbonClientName
    private String name = "client";
    @Autowired
    private PropertiesFactory propertiesFactory;

    public StructureRibbonClientConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public IClientConfig ribbonClientConfig() {
        DefaultClientConfigImpl config = new DefaultClientConfigImpl();
        config.loadProperties(this.name);
        config.set(CommonClientConfigKey.ConnectTimeout, DEFAULT_CONNECT_TIMEOUT);
        config.set(CommonClientConfigKey.ReadTimeout, DEFAULT_READ_TIMEOUT);
        config.set(CommonClientConfigKey.GZipPayload, DEFAULT_GZIP_PAYLOAD);
        return config;
    }

    @Bean
    @ConditionalOnMissingBean
    public IRule ribbonRule(IClientConfig config) {
        if (this.propertiesFactory.isSet(IRule.class, this.name)) {
            return (IRule) this.propertiesFactory.get(IRule.class, config, this.name);
        } else {
            ZoneAvoidanceRule rule = new ZoneAvoidanceRule();
            rule.initWithNiwsConfig(config);
            return rule;
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public IPing ribbonPing(IClientConfig config) {
        return (IPing) (this.propertiesFactory.isSet(IPing.class, this.name) ? (IPing) this.propertiesFactory.get(IPing.class, config, this.name) : new DummyPing());
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerList<Server> ribbonServerList(IClientConfig config) {
        StructureServer[] serverList = serverProperties.getServer()
                .stream()
                .map(this::server)
                .collect(Collectors.toList())
                .toArray(new StructureServer[serverProperties.getServer().size()]);
        StructureStaticServerList structureStaticServerList = new StructureStaticServerList(serverList);
        structureStaticServerList.initWithNiwsConfig(config);
        return structureStaticServerList;
    }

    private StructureServer server(ServerProperties.StaticServer server) {
        StructureServer structureServer = new StructureServer(server.getHost(), server.getPort());
        structureServer.setAlive(true);
        structureServer.setReadyToServe(true);
        structureServer.setZone((null == server.getZone()) ? Server.UNKNOWN_ZONE : server.getZone());
        Server.MetaInfo metaInfo = new StructureServer.MetaInfo(server.getId(), server.getServerGroup(), server.getServiceIdForDiscovery(), server.getId());
        structureServer.setMetaInfo(metaInfo);
        return structureServer;

    }

    @Bean
    @ConditionalOnMissingBean
    public ServerListUpdater ribbonServerListUpdater(IClientConfig config) {
        return new PollingServerListUpdater(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoadBalancer ribbonLoadBalancer(IClientConfig config, ServerList<Server> serverList, ServerListFilter<Server> serverListFilter, IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
        return (ILoadBalancer) (this.propertiesFactory.isSet(ILoadBalancer.class, this.name) ? (ILoadBalancer) this.propertiesFactory.get(ILoadBalancer.class, config, this.name) : new ZoneAwareLoadBalancer(config, rule, ping, serverList, serverListFilter, serverListUpdater));
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerListFilter<Server> ribbonServerListFilter(IClientConfig config) {
        if (this.propertiesFactory.isSet(ServerListFilter.class, this.name)) {
            return (ServerListFilter) this.propertiesFactory.get(ServerListFilter.class, config, this.name);
        } else {
            ZonePreferenceServerListFilter filter = new ZonePreferenceServerListFilter();
            filter.initWithNiwsConfig(config);
            return filter;
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public RibbonLoadBalancerContext ribbonLoadBalancerContext(ILoadBalancer loadBalancer, IClientConfig config, RetryHandler retryHandler) {
        return new RibbonLoadBalancerContext(loadBalancer, config, retryHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryHandler retryHandler(IClientConfig config) {
        return new DefaultLoadBalancerRetryHandler(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerIntrospector serverIntrospector() {
        return new DefaultServerIntrospector();
    }

    @PostConstruct
    public void preprocess() {
        RibbonUtils.setRibbonProperty(this.name, CommonClientConfigKey.DeploymentContextBasedVipAddresses.key(), this.name);
    }
}
