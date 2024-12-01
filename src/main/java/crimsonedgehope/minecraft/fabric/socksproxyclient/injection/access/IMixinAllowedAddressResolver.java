package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.doh.DOHResolver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IMixinAllowedAddressResolver {
    DOHResolver socksProxyClient$getDohResolver();
    void socksProxyClient$setDohResolver();
}
