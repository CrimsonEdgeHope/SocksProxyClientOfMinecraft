package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access;

import java.net.InetSocketAddress;

public interface IClientConnectionMixin {
    void socksProxyClient$setRemote(InetSocketAddress socketAddress);
    InetSocketAddress socksProxyClient$getRemote();
}
