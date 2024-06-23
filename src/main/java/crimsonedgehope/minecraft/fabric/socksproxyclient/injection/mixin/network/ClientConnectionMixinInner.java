package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IClientConnectionMixin;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ClientConnectionProxySelection;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.net.InetSocketAddress;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.network.ClientConnection$1")
public class ClientConnectionMixinInner {
    @WrapOperation(method = "initChannel",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/netty/channel/ChannelPipeline;addLast(Ljava/lang/String;Lio/netty/channel/ChannelHandler;)Lio/netty/channel/ChannelPipeline;",
                    ordinal = 1
            ),
            remap = false
    )
    private ChannelPipeline wrapped(ChannelPipeline pipeline, String handlerName, ChannelHandler channelHandler, Operation<ChannelPipeline> original) {
        ChannelPipeline ret = original.call(pipeline, handlerName, channelHandler);
        InetSocketAddress remote = ((IClientConnectionMixin) (ClientConnection) channelHandler).socksProxyClient$getInetSocketAddress();
        ClientConnectionProxySelection.fire(remote, pipeline);
        return ret;
    }
}
