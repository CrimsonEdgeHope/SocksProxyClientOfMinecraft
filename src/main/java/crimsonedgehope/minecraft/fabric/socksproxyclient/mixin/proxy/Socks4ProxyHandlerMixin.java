package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.proxy.Socks4ProxyHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Environment(EnvType.CLIENT)
@Mixin(Socks4ProxyHandler.class)
public abstract class Socks4ProxyHandlerMixin extends ProxyHandlerMixin {

    @Inject(method = "newInitialMessage", at = @At("HEAD"), remap = false)
    private void injected(ChannelHandlerContext ctx, CallbackInfoReturnable<Object> cir) {
        if (destinationAddress().equals(proxyAddress())) {
            throw new IllegalArgumentException("You are not supposed to connect to the Socks4 proxy through itself.");
        }
    }
}
