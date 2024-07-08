package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IConnectScreenMixin {
    void socksProxyClient$setHost(String host);
    String socksProxyClient$getHost();
}
