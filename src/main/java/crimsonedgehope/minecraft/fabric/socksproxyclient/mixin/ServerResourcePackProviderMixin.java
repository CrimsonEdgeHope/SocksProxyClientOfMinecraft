package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpToSocksServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.ServerResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(ServerResourcePackProvider.class)
public class ServerResourcePackProviderMixin {
    @Redirect(method = "download", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getNetworkProxy()Ljava/net/Proxy;"))
    private Proxy redirectedGet(MinecraftClient instance) {
        if (!HttpToSocksServer.INSTANCE.isFired()) {
            return Proxy.NO_PROXY;
        }
        return new Proxy(Proxy.Type.HTTP, HttpToSocksServer.INSTANCE.getChannel().localAddress());
    }
}
