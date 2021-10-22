//package com.my.liufeng.rpc.registry;
//
//import com.my.liufeng.rpc.utils.IpUtil;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @Author liufeng
// * @Description: 使用注册中心和负载均衡组件才需要，暂时不用
// * @since 2021/5/27 20:09
// */
//public abstract class AbstractRegistryClient {
//    protected Map<String, List<ServerAddress>> addressMap;
//
//    /**
//     * 根据服务名获取服务列表
//     *
//     * @param serviceName 服务名
//     * @return 服务列表
//     */
//    public List<ServerAddress> getServerList(String serviceName) {
//        List<ServerAddress> serverAddressList = addressMap.get(serviceName);
//        if (serverAddressList == null) {
//            List<String> addressList = getFormServer(serviceName);
//            if (addressList != null && addressList.size() > 0) {
//                serverAddressList = new LinkedList<>();
//                for (String address : addressList) {
//                    serverAddressList.add(new ServerAddress(address));
//                }
//                addressMap.put(serviceName, serverAddressList);
//            }
//        }
//        return serverAddressList;
//    }
//
//    protected abstract List<String> getFormServer(String serviceName);
//
//    class ServerAddress {
//        private String ip;
//        private Integer port;
//
//        public ServerAddress(String ip, Integer port) {
//            this.ip = ip;
//            this.port = port;
//        }
//
//        public ServerAddress(String address) {
//            String[] serverInfo = IpUtil.splitAddress(address);
//            this.ip = serverInfo[0];
//            this.port = Integer.parseInt(serverInfo[1]);
//        }
//    }
//}
