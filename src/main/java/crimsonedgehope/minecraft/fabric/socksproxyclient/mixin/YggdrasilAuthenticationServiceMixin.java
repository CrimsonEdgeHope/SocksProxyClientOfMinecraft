package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpToSocksServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Mixin(YggdrasilAuthenticationService.class)
public class YggdrasilAuthenticationServiceMixin extends HttpAuthenticationServiceMixin {
    @Override
    protected void redirectedGet(CallbackInfoReturnable<Proxy> cir) {
        cir.cancel();
        if (!HttpToSocksServer.INSTANCE.isFired()) {
            cir.setReturnValue(Proxy.NO_PROXY);
        } else {
            cir.setReturnValue(new Proxy(Proxy.Type.HTTP, HttpToSocksServer.INSTANCE.getChannel().localAddress()));
        }
    }
}
