package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin.viafabricplus;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.access.IAllowedAddressResolverMixin;
import de.florianmichael.viafabricplus.definition.v1_14_4.LegacyServerAddress;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AllowedAddressResolver;
import net.minecraft.client.network.RedirectResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(LegacyServerAddress.class)
public class LegacyServerAddressMixin {
    @Redirect(method = "parse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/AllowedAddressResolver;redirectResolver:Lnet/minecraft/client/network/RedirectResolver;"))
    private static RedirectResolver redirected(AllowedAddressResolver instance) {
        SocksProxyClient.LOGGER.debug("Mixin into class LegacyServerAddress from mod ViaFabricPlus");
        return ((IAllowedAddressResolverMixin) instance).socksProxyClient$getRedirectResolver(instance);
    }
}
