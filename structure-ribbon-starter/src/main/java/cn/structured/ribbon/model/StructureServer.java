package cn.structured.ribbon.model;

import com.netflix.loadbalancer.Server;

/**
 * 服务信息
 *
 * @author cqliut
 * @version 2022.1227
 * @since 1.0.1
 */
public class StructureServer extends Server {

    @Override
    public Server.MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(Server.MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    private Server.MetaInfo metaInfo;

    public StructureServer(String host, int port) {
        super(host, port);
    }

    public StructureServer(String scheme, String host, int port) {
        super(scheme, host, port);
    }

    public StructureServer(String id) {
        super(id);
    }

    public static class MetaInfo implements Server.MetaInfo {

        private String appName;

        private String serverGroup;

        private String serviceIdForDiscovery;

        private String instanceId;

        public MetaInfo(String appName, String serverGroup, String serviceIdForDiscovery, String instanceId) {
            this.appName = appName;
            this.serverGroup = serverGroup;
            this.serviceIdForDiscovery = serviceIdForDiscovery;
            this.instanceId = instanceId;
        }

        @Override
        public String getAppName() {
            return this.appName;
        }

        @Override
        public String getServerGroup() {
            return this.serverGroup;
        }

        @Override
        public String getServiceIdForDiscovery() {
            return this.serviceIdForDiscovery;
        }

        @Override
        public String getInstanceId() {
            return this.instanceId;
        }
    }
}
