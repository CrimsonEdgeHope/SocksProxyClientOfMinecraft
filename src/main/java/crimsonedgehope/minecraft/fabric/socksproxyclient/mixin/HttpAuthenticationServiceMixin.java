package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import com.mojang.authlib.HttpAuthenticationService;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(HttpAuthenticationService.class)
public abstract class HttpAuthenticationServiceMixin {
    @Inject(method = "getProxy", at = @At("RETURN"), cancellable = true, remap = false)
    protected void redirectedGet(CallbackInfoReturnable<Proxy> cir) {

    }
}
