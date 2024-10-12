package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class AuthlibMinecraftClientMixin {
    @Redirect(method = "createUrlConnection",
            at = @At(value = "FIELD", target = "Lcom/mojang/authlib/minecraft/client/MinecraftClient;proxy:Ljava/net/Proxy;",
                    opcode = Opcodes.GETFIELD),
            remap = false
    )
    private Proxy redirected(MinecraftClient instance) {
        return HttpProxyUtils.getProxyObject(ServerConfig.shouldProxyYggdrasil());
    }
}
