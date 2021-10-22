### 根据博客，自己实现rpc调用

#### 2021-10-22 实现第一个简单版本，实现请求/相应功能
1. Netty通信，NioSocketChannel,StringEncoder,StringDecoder,ChannelInboundHandlerAdapter
2. JDK动态代理，代理远程接口调用
3. Fastjson序列化，RpcRequest/RpcResponse
4. 自定义容器MethodRegistry管理远程调用接口，interface使用@MethodStub标识
5. 自定义容器ServiceRegistry管理服务启功者，service使用@RpcService标识
6. System.out.println() 还没有去掉
