package crimsonedgehope.minecraft.fabric.socksproxyclient.access;

import java.net.InetSocketAddress;

public interface IClientConnectionMixin {
    void socksProxyClient$setInetSocketAddress(InetSocketAddress inetSocketAddress);
    InetSocketAddress socksProxyClient$getInetSocketAddress();
}
