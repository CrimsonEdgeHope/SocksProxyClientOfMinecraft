package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.net.InetSocketAddress;

@Environment(EnvType.CLIENT)
public interface IMixinClientConnection {
    void socksProxyClient$setInetSocketAddress(InetSocketAddress inetSocketAddress);
    InetSocketAddress socksProxyClient$getInetSocketAddress();
}
