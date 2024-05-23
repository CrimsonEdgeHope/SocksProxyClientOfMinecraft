package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpToSocksServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Mutable
    @Shadow @Final private Proxy networkProxy;

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;networkProxy:Ljava/net/Proxy;", opcode = Opcodes.PUTFIELD))
    private void redirectedPut(MinecraftClient instance, Proxy value) {
        this.networkProxy = Proxy.NO_PROXY;
    }

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;networkProxy:Ljava/net/Proxy;", opcode = Opcodes.GETFIELD))
    private Proxy redirectedGet(MinecraftClient instance) {
        if (!HttpToSocksServer.INSTANCE.isFired()) {
            return Proxy.NO_PROXY;
        }
        return new Proxy(Proxy.Type.HTTP, HttpToSocksServer.INSTANCE.getChannel().localAddress());
    }

    @Inject(method = "getNetworkProxy", at = @At("RETURN"), cancellable = true)
    private void redirectedGet(CallbackInfoReturnable<Proxy> cir) {
        cir.cancel();
        cir.setReturnValue(redirectedGet(MinecraftClient.getInstance()));
    }
}
