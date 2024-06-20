package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.xbill.DNS.DohResolver;

@Environment(EnvType.CLIENT)
public interface IAllowedAddressResolverMixin {
    DohResolver socksProxyClient$getDohResolver();
    void socksProxyClient$setDohResolver();
}
