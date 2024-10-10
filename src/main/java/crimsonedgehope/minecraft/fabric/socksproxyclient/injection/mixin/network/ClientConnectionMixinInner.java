package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.SocksApply;
import io.netty.channel.ChannelPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.network.ClientConnection$1")
public class ClientConnectionMixinInner {
    @WrapOperation(method = "initChannel", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;addFlowControlHandler(Lio/netty/channel/ChannelPipeline;)V"))
    private void wrapped(ClientConnection instance, ChannelPipeline pipeline, Operation<Void> original) {
        original.call(instance, pipeline);
        SocksApply.fire(instance, pipeline);
    }
}
