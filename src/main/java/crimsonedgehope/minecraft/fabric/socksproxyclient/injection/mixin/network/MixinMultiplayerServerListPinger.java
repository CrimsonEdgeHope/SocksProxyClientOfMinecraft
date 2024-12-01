package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.netty.AccessorChannelInitializer;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.socks.SocksSelection;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.MultiplayerServerListPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@Mixin(MultiplayerServerListPinger.class)
@Environment(EnvType.CLIENT)
public class MixinMultiplayerServerListPinger {

    @WrapOperation(method = "ping",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;",
                    remap = false
            )
    )
    private ChannelFuture injected(Bootstrap instance, InetAddress inetHost, int inetPort, Operation<ChannelFuture> original) {
        final InetSocketAddress remote = new InetSocketAddress(inetHost, inetPort);
        final ChannelInitializer initializer = (ChannelInitializer) instance.config().handler();
        instance.handler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ((AccessorChannelInitializer) initializer).invokeInitChannel(ch);
                SocksSelection.fire(remote, ch.pipeline());
            }
        });
        return original.call(instance, inetHost, inetPort);
    }
}
