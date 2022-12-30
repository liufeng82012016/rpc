### 根据博客，自己实现rpc调用

#### 2021-10-23-- 迭代第二个版本
1. Netty通信，NioSocketChannel,ChannelInboundHandlerAdapter；自定义编码器/解码器，不再使用StringEncoder/StringDecoder
2. JDK动态代理，代理远程接口调用
3. 增加序列化方式
4. 使用CompletableFuture接收响应，增加方法调用超时设置
5. 编码器/解码器使用分隔符，自动丢弃非法内容
#### 包结构
- annotation 自定义注解
- constants 常量
- context 伪容器
- enums 枚举
- model rpc消息实体类
- netty 使用netty相关的实现，包含client/server/codec/handler
- registry 服务注册/发现/负载均衡  -- 现在没有用起来，写死的
- serial 序列化
- utils 自定义工具类
- mock 模拟数据

#### 启动步骤
1. StartServer.main();
2. StartClient.main();
3. // 如果server的端口修改掉，需要改掉mock包下RemoteHelloService的serverAddress

#### 心跳实现

#### 注意点
1. netty很多地方使用了Future，需要根据业务对返回值进行处理，否则一些错误无法发现
