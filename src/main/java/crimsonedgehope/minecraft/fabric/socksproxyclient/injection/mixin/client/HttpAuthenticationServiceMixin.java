package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.client;

import com.mojang.authlib.HttpAuthenticationService;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(HttpAuthenticationService.class)
public abstract class HttpAuthenticationServiceMixin {
    @Redirect(method = "getProxy",
            at = @At(value = "FIELD", target = "Lcom/mojang/authlib/HttpAuthenticationService;proxy:Ljava/net/Proxy;", opcode = Opcodes.GETFIELD),
            remap = false
    )
    protected abstract Proxy redirectedGet0(HttpAuthenticationService instance);
}
