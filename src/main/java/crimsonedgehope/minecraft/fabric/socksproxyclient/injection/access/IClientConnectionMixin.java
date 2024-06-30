package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access;

import java.net.InetSocketAddress;

public interface IClientConnectionMixin {
    void socksProxyClient$setInetSocketAddress(InetSocketAddress socketAddress);
    InetSocketAddress socksProxyClient$getInetSocketAddress();
}
