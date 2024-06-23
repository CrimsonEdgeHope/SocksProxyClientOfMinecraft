package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ClientConnectionProxySelection;
import io.netty.channel.Channel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(targets = "net.minecraft.client.network.MultiplayerServerListPinger$2")
public class MultiplayerServerListPingerMixinInner {

    @Shadow(aliases = "field_3778") @Final
    private InetSocketAddress inetSocketAddress;

    @Inject(method = "initChannel",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/netty/channel/ChannelPipeline;addLast([Lio/netty/channel/ChannelHandler;)Lio/netty/channel/ChannelPipeline;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void injected(Channel channel, CallbackInfo ci) {
        ClientConnectionProxySelection.fire(inetSocketAddress, channel.pipeline());
    }
}
