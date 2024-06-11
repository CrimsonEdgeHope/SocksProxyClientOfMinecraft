package crimsonedgehope.minecraft.fabric.socksproxyclient.access;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AllowedAddressResolver;
import net.minecraft.client.network.RedirectResolver;
import org.xbill.DNS.DohResolver;

@Environment(EnvType.CLIENT)
public interface IAllowedAddressResolverMixin {
    DohResolver socksProxyClient$getDohResolver();
    void socksProxyClient$setDohResolver();

    RedirectResolver socksProxyClient$getRedirectResolver(AllowedAddressResolver instance);
}
