package crimsonedgehope.minecraft.fabric.socksproxyclient.access;

import io.netty.channel.ChannelHandler;

public interface IClientConnectionMixinInner {
    void socksProxyClient$setClientConnection(ChannelHandler clientConnection);
    ChannelHandler socksProxyClient$getClientConnection();
}
