package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.client;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.http.HttpProxyUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.PlayerSkinTextureDownloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(PlayerSkinTextureDownloader.class)
public class MixinPlayerSkinTexture {
    @Redirect(method = "download", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getNetworkProxy()Ljava/net/Proxy;"))
    private static Proxy redirectedGet(MinecraftClient instance) {
        return HttpProxyUtils.getProxyObject(ServerConfig.shouldProxyPlayerSkinDownload());
    }
}
