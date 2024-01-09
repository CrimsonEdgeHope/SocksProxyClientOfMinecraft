package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import io.netty.channel.Channel;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

@Mixin(targets = "net.minecraft.network.ClientConnection$1")
public class ClientMixin {
	@Inject(at = @At(value = "TAIL"), method = "initChannel")
	private void injected(Channel channel, CallbackInfo info) {
		Proxy proxy = MinecraftClient.getInstance().getNetworkProxy();
		String s1 = SocksProxyClient.Auth.getUsername();
		String s2 = SocksProxyClient.Auth.getPassword();
		channel.pipeline().addFirst(new Socks5ProxyHandler(proxy.address(), s1, s2));
	}
}
