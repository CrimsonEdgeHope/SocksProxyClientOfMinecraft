package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.client;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyServerUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.RealmsClientConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(RealmsClientConfig.class)
public class RealmsClientConfigMixin {
    @Redirect(
            method = "getProxy",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/realms/RealmsClientConfig;proxy:Ljava/net/Proxy;",
                    opcode = Opcodes.GETSTATIC)
    )
    private static Proxy redirected() {
        return HttpProxyServerUtils.getProxyObject(true);
    }
}
