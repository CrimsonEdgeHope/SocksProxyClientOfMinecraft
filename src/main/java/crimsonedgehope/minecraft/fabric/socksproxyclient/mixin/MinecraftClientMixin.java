package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.mixin_variables.MinecraftClientMixinVariables;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyServerUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Mutable
    @Shadow @Final private Proxy networkProxy;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/yggdrasil/YggdrasilAuthenticationService;<init>(Ljava/net/Proxy;)V", shift = At.Shift.BEFORE, remap = false))
    private void injected(RunArgs args, CallbackInfo ci) {
        MinecraftClientMixinVariables.setRunArgs(args);
    }

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;networkProxy:Ljava/net/Proxy;", opcode = Opcodes.PUTFIELD))
    private void redirectedPut(MinecraftClient instance, Proxy value) {
        this.networkProxy = Proxy.NO_PROXY;
    }

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;networkProxy:Ljava/net/Proxy;", opcode = Opcodes.GETFIELD))
    private Proxy redirectedGet(MinecraftClient instance) {
        return HttpProxyServerUtils.getProxyObject();
    }
}
