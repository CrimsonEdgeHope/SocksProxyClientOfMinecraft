package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.client;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyServerUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.server.ServerResourcePackLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(ServerResourcePackLoader.class)
public class ServerResourcePackLoaderMixin {
    @ModifyArg(
            method = "createDownloadQueuer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resource/server/ServerResourcePackLoader$4;<init>(Lnet/minecraft/client/resource/server/ServerResourcePackLoader;Lnet/minecraft/client/session/Session;Lnet/minecraft/util/Downloader;Ljava/net/Proxy;Ljava/util/concurrent/Executor;)V"
            )
    )
    private Proxy redirectedGet(Proxy instance) {
        return HttpProxyServerUtils.getProxyObject(ServerConfig.shouldProxyServerResourceDownload());
    }
}
