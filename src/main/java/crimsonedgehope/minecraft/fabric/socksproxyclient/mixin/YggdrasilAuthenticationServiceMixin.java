package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyServerUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(YggdrasilAuthenticationService.class)
public class YggdrasilAuthenticationServiceMixin extends HttpAuthenticationServiceMixin {
    @Override
    protected Proxy redirectedGet0(HttpAuthenticationService instance) {
        return HttpProxyServerUtils.getProxyObject(ServerConfig.shouldProxyYggdrasil());
    }
}
