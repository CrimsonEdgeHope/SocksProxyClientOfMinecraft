package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ServerAddress;

@Environment(EnvType.CLIENT)
public interface IConnectScreenMixinInner {
    void socksProxyClient$setServerAddress(ServerAddress serverAddress);
    ServerAddress socksProxyClient$getServerAddress();
}
