package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access;

import io.netty.channel.ChannelHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IClientConnectionMixinInner {
    void socksProxyClient$setClientConnection(ChannelHandler clientConnection);
    ChannelHandler socksProxyClient$getClientConnection();
}
