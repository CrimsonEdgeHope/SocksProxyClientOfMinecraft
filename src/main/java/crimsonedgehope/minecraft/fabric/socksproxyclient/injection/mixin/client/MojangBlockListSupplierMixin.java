package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.client;

import com.mojang.patchy.MojangBlockListSupplier;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Environment(EnvType.CLIENT)
@Mixin(MojangBlockListSupplier.class)
public class MojangBlockListSupplierMixin {
    @Redirect(
            method = "createBlockList",
            at = @At(value = "INVOKE", target = "Ljava/net/URL;openConnection()Ljava/net/URLConnection;"),
            remap = false
    )
    private URLConnection redirectedGet(URL instance) throws IOException {
        return instance.openConnection(HttpProxyUtils.getProxyObject(ServerConfig.shouldProxyBlockListSupplier()));
    }
}
