package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ProxyConfig;
import io.netty.channel.Channel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.network.ClientConnection$1")
public class ClientConnectionChannelMixin {
	@Inject(at = @At(value = "TAIL"), method = "initChannel")
	private void injected(Channel channel, CallbackInfo info) {
		Proxy proxy = MinecraftClient.getInstance().getNetworkProxy();
		String s1 = ProxyConfig.getUsername();
		String s2 = ProxyConfig.getPassword();
		if (proxy.address() != null) {
			switch (ProxyConfig.getSocksVersion()) {
				case 4:
					SocksProxyClient.LOGGER.debug("Using Socks4");
					channel.pipeline().addFirst("socks", new Socks4ProxyHandler(proxy.address(), s1));
					break;
				case 5:
				default:
					SocksProxyClient.LOGGER.debug("Using Socks5");
					channel.pipeline().addFirst("socks", new Socks5ProxyHandler(proxy.address(), s1, s2));
					break;
			}
		}
	}
}
