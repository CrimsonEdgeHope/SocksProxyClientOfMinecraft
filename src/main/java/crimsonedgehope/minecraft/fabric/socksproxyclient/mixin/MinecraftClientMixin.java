package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyServerUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Mutable
    @Shadow @Final private Proxy netProxy;

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;netProxy:Ljava/net/Proxy;", opcode = Opcodes.PUTFIELD))
    private void redirectedPut(MinecraftClient instance, Proxy value) {
        this.netProxy = HttpProxyServerUtils.getProxyObject();
    }

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;netProxy:Ljava/net/Proxy;", opcode = Opcodes.GETFIELD))
    private Proxy redirectedGet(MinecraftClient instance) {
        return HttpProxyServerUtils.getProxyObject();
    }
}
