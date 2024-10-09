package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Getter
public class HttpToSocksServer {

    public static final HttpToSocksServer INSTANCE;

    private static final Logger LOGGER = SocksProxyClient.logger("HttpProxy");

    static {
        INSTANCE = new HttpToSocksServer();
    }

    private final String host;
    private int port;
    private Channel channel;
    private boolean fired = false;

    public HttpToSocksServer() {
        this(0);
    }

    public HttpToSocksServer(int port) {
        this("localhost", port);
    }

    public HttpToSocksServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void fire() {
        fire(f -> {});
    }

    public void fire(final GenericFutureListener<Future<? super Void>> firedCallback) {
        if (!GeneralConfig.usingProxy()) {
            LOGGER.info("Not starting internal http proxy.");
            return;
        }

        if (fired) {
            return;
        }

        new Thread(() -> {
            EventLoopGroup acceptorGroup = new NioEventLoopGroup(1);
            EventLoopGroup clientGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                ChannelFuture future = b.group(acceptorGroup, clientGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler())
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel channel) {
                                channel.pipeline()
                                        .addLast("http_aggre", new HttpObjectAggregator(262144000))
                                        .addLast("http_req_dec", new HttpRequestDecoder())
                                        .addLast("opentunnel", new HttpProxyClientInboundHandler());
                            }
                        })
                        .bind(host, port).sync();
                channel = future.addListener(f -> {
                    if (f.isSuccess()) {
                        port = ((InetSocketAddress) future.channel().localAddress()).getPort();
                        fired = true;
                        LOGGER.info("Internal http proxy listening on {}", future.channel().localAddress());
                    }
                }).addListener(firedCallback).channel();
                channel = channel.closeFuture().sync().addListener(f -> {
                    LOGGER.info("Internal http proxy shutting off!");
                }).channel();
            } catch (Exception e) {
                LOGGER.error("Error starting internal http proxy!", e);
            } finally {
                cease();
                acceptorGroup.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS);
                clientGroup.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS);
            }
        }).start();
    }

    public void cease() {
        if (!fired) {
            return;
        }
        fired = false;
        if (channel != null) {
            channel.close();
            channel = null;
        }
        this.port = 0;
    }

    @ChannelHandler.Sharable
    private static final class HttpProxyClientInboundHandler extends SimpleChannelInboundHandler<HttpRequest> {
        private Channel remote;
        private Channel client;

        private boolean connectMethod = false;
        private HttpVersion httpVersion;
        private String remoteHttpHost = null;
        private int remoteHttpPort = -1;

        private boolean parsed = false;

        @Override
        public void channelActive(@NotNull ChannelHandlerContext ctx) {
            this.client = ctx.channel();
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) {
            if (!parsed) {
                connectMethod = msg.method().equals(HttpMethod.CONNECT);
                httpVersion = msg.protocolVersion();

                int k;

                remoteHttpHost = Objects.requireNonNull(msg.headers().getAsString("host"));

                k = remoteHttpHost.indexOf(":");
                if (k == -1) {
                    if (connectMethod) {
                        remoteHttpPort = 443;
                    } else {
                        remoteHttpPort = 80;
                    }
                } else {
                    remoteHttpPort = Integer.parseInt(Objects.requireNonNull(remoteHttpHost.substring(k + 1)));
                    remoteHttpHost = remoteHttpHost.substring(0, k);
                }

                parsed = true;
            }

            if (remote == null) {
                boolean noResolver = ServerConfig.httpRemoteResolve();
                ChannelHandler handler;
                Proxy proxySelection = GeneralConfig.getProxy();
                Credential proxyCredential = GeneralConfig.getProxyCredential();
                if (proxySelection.equals(Proxy.NO_PROXY)) {
                    handler = new ChannelDuplexHandler();
                    noResolver = false;
                } else {
                    switch (GeneralConfig.getSocksVersion()) {
                        case SOCKS4 -> {
                            LOGGER.debug("http - Socks4. Remote: {}:{}", remoteHttpHost, remoteHttpPort);
                            handler = SocksUtils.getSocks4ProxyHandler(proxySelection.address(), proxyCredential);
                            noResolver = false;
                        }
                        case SOCKS5 -> {
                            LOGGER.debug("http - Socks5. Remote: {}:{}", remoteHttpHost, remoteHttpPort);
                            handler = SocksUtils.getSocks5ProxyHandler(proxySelection.address(), proxyCredential);
                        }
                        default -> {
                            LOGGER.debug("http. Remote: {}:{}", remoteHttpHost, remoteHttpPort);
                            handler = new ChannelDuplexHandler();
                            noResolver = false;
                        }
                    }
                }

                Bootstrap b = new Bootstrap().group(client.eventLoop())
                        .channel(client.getClass())
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(@NotNull SocketChannel channel) {
                                channel.pipeline().addFirst(handler);
                            }
                        })
                        .option(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.SO_KEEPALIVE, true);
                if (noResolver) {
                    b = b.disableResolver();
                }
                LOGGER.debug("noResolver: {}", noResolver);
                ChannelFuture future = b.connect(remoteHttpHost, remoteHttpPort);
                future.addListener(f -> {
                    if (f.isSuccess()) {
                        remote = future.channel();
                        if (connectMethod) {
                            client.pipeline().addLast("temp_http_res_enc", new HttpResponseEncoder());
                            client.writeAndFlush(
                                    new DefaultHttpResponse(
                                            httpVersion,
                                            new HttpResponseStatus(200, "Connection Established")
                                    )
                            ).addListener(f0 -> {
                                if (f0.isSuccess()) {
                                    client.pipeline().remove("temp_http_res_enc");
                                    channelRemoval();
                                    channelTakeover();
                                    LOGGER.info("Open tunnel to remote {}:{}", remoteHttpHost, remoteHttpPort);
                                }
                            }).addListeners(
                                    ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE,
                                    ChannelFutureListener.CLOSE_ON_FAILURE
                            );
                        } else {
                            remote.pipeline().addLast("temp_http_req_enc", new HttpRequestEncoder());
                            remote.writeAndFlush(msg).addListener(f0 -> {
                                if (f0.isSuccess()) {
                                    remote.pipeline().remove("temp_http_req_enc");
                                    channelRemoval();
                                    channelTakeover();
                                    LOGGER.info("Open tunnel to remote {}:{}", remoteHttpHost, remoteHttpPort);
                                }
                            }).addListeners(
                                    ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE,
                                    ChannelFutureListener.CLOSE_ON_FAILURE
                            );
                        }
                    } else {
                        shutOffActiveChannel(client);
                    }
                }).addListeners(
                        ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE,
                        ChannelFutureListener.CLOSE_ON_FAILURE
                );
                remote = future.channel();
                client = client.closeFuture().addListener(f -> {
                    if (f.isDone()) {
                        shutOffActiveChannel(remote);
                    }
                }).channel();
                remote = remote.closeFuture().addListener(f -> {
                    if (f.isDone()) {
                        LOGGER.info("Tunnel to remote {}:{} closed.", remoteHttpHost, remoteHttpPort);
                        shutOffActiveChannel(client);
                    }
                }).channel();
            }
        }

        private void channelRemoval() {
            client.pipeline().remove("http_aggre");
            client.pipeline().remove("http_req_dec");
        }

        private void channelTakeover() {
            clientChannelTakeover();
            remoteChannelTakeover();
        }

        private void clientChannelTakeover() {
            client.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                @Override
                public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
                    remote.writeAndFlush(msg.retain()).addListeners(
                            ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE,
                            ChannelFutureListener.CLOSE_ON_FAILURE
                    );
                }

                @Override
                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                    LOGGER.error("Error in client channel!", cause);
                    shutOffActiveChannel(ctx.channel());
                }
            });
        }

        private void remoteChannelTakeover() {
            remote.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                @Override
                public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
                    client.writeAndFlush(msg.retain()).addListeners(
                            ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE,
                            ChannelFutureListener.CLOSE_ON_FAILURE
                    );
                }

                @Override
                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                    LOGGER.error("Error in remote channel!", cause);
                    shutOffActiveChannel(ctx.channel());
                }
            });
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            LOGGER.error("Error running internal http proxy!", cause);
            shutOffActiveChannel(ctx.channel());
        }

        @Override
        public void channelInactive(@NotNull ChannelHandlerContext ctx) {
            shutOffActiveChannel(remote);
        }

        private void shutOffActiveChannel(Channel channel) {
            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}
