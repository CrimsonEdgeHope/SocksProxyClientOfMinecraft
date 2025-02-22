package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.client;

import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.http.HttpProxyUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(YggdrasilAuthenticationService.class)
public class MixinYggdrasilAuthenticationService extends MixinHttpAuthenticationService {
    @Override
    protected Proxy redirectedGet0(HttpAuthenticationService instance) {
        return HttpProxyUtils.getProxyObject(ServerConfig.shouldProxyYggdrasil());
    }
}
