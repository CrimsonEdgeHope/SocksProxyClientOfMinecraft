package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyServerUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(YggdrasilAuthenticationService.class)
public class YggdrasilAuthenticationServiceMixin extends HttpAuthenticationServiceMixin {
    @Override
    protected void redirectedGet(CallbackInfoReturnable<Proxy> cir) {
        cir.cancel();
        cir.setReturnValue(HttpProxyServerUtils.getProxyObject(ServerConfig.shouldProxyYggdrasilAuth()));
    }
}
