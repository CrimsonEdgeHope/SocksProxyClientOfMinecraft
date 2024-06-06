package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.mixin.MinecraftClientAccessor;
import crimsonedgehope.minecraft.fabric.socksproxyclient.mixin_variables.MinecraftClientMixinVariables;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.texture.PlayerSkinProvider;

import java.io.File;
import java.net.Proxy;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpProxyServerUtils {

    public static Proxy getProxyObject() {
        return getProxyObject(true);
    }

    public static Proxy getProxyObject(boolean useProxy) {
        SocksProxyClient.LOGGER.debug("getProxyObject: {}", useProxy);
        if (!useProxy || !HttpToSocksServer.INSTANCE.isFired()) {
            return Proxy.NO_PROXY;
        } else {
            return new Proxy(Proxy.Type.HTTP, HttpToSocksServer.INSTANCE.getChannel().localAddress());
        }
    }

    public static CompletableFuture<Void> recreateYggdrasilService() {
        return HttpProxyServerUtils.createYggdrasilService();
    }

    public static CompletableFuture<Void> createYggdrasilService() {
        final MinecraftClient client = MinecraftClient.getInstance();
        return client.submit(() -> {
            SocksProxyClient.LOGGER.debug("createYggdrasilService");
            SocksProxyClient.LOGGER.info("Attempt to recreate Yggdrasil service");
            HttpToSocksServer.INSTANCE.cease();
            HttpToSocksServer.INSTANCE.fire(future -> {
                if (future.isSuccess()) {
                    RunArgs args = MinecraftClientMixinVariables.getRunArgs();
                    MinecraftClientAccessor accessor = ((MinecraftClientAccessor) client);
                    YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(HttpProxyServerUtils.getProxyObject());
                    accessor.setSessionService(yggdrasilAuthenticationService.createMinecraftSessionService());
                    accessor.setSocialInteractionsService(accessor.invokeCreateSocialInteractionsService(yggdrasilAuthenticationService, args));
                    accessor.setSkinProvider(new PlayerSkinProvider(accessor.getTextureManager(), new File(args.directories.assetDir, "skins"), accessor.getSessionService()));
                    accessor.setSocialInteractionsManager(new SocialInteractionsManager(client, accessor.getSocialInteractionService()));
                    SocksProxyClient.LOGGER.info("Recreated Yggdrasil service.");
                } else {
                    SocksProxyClient.LOGGER.warn("Failed to recreate Yggdrasil service.");
                }
            });
        });
    }
}
