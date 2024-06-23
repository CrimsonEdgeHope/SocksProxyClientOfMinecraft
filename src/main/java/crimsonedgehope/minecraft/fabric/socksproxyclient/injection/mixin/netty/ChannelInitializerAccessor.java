package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(ChannelInitializer.class)
public interface ChannelInitializerAccessor<C extends Channel> {
    @Invoker(value = "initChannel", remap = false)
    void invokeInitChannel(C ch);
}
