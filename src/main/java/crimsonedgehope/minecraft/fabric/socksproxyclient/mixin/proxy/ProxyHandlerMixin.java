package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin.proxy;

import io.netty.handler.proxy.ProxyHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.SocketAddress;

@Environment(EnvType.CLIENT)
@Mixin(ProxyHandler.class)
public abstract class ProxyHandlerMixin {
    /**
     * After tedious rounds of investigation, presumably,
     * combination with ViaFabricPlus could fire a connection to the SOCKS proxy through itself.
     * When opening multiplayer screen for the first time in a game session, automatic pinging
     * a reachable server can be seen twice, between which such undesired proxying behavior occurs.
     * Root cause unclear.
     */

    @Shadow(remap = false)
    public abstract <T extends SocketAddress> T proxyAddress();

    @Shadow(remap = false)
    public abstract <T extends SocketAddress> T destinationAddress();
}
