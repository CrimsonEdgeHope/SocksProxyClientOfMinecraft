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
import net.minecraft.client.session.ProfileKeys;
import net.minecraft.client.session.report.AbuseReportContext;
import net.minecraft.client.session.report.ReporterEnvironment;
import net.minecraft.client.session.telemetry.TelemetryManager;
import net.minecraft.client.texture.PlayerSkinProvider;

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
                    accessor.setAuthenticationService(new YggdrasilAuthenticationService(HttpProxyServerUtils.getProxyObject()));
                    accessor.setSessionService(accessor.getAuthenticationService().createMinecraftSessionService());
                    accessor.setUserApiService(accessor.invokeCreateUserApiService(accessor.getAuthenticationService(), args));
                    accessor.setSkinProvider(new PlayerSkinProvider(accessor.getTextureManager(), args.directories.assetDir.toPath().resolve("skins"), accessor.getSessionService(), client));
                    accessor.setSocialInteractionsManager(new SocialInteractionsManager(client, accessor.getUserApiService()));
                    accessor.setTelemetryManager(new TelemetryManager(client, accessor.getUserApiService(), args.network.session));
                    accessor.setProfileKeys(ProfileKeys.create(accessor.getUserApiService(), args.network.session, client.runDirectory.toPath()));
                    accessor.setAbuseReportContext(AbuseReportContext.create(ReporterEnvironment.ofIntegratedServer(), accessor.getUserApiService()));
                    SocksProxyClient.LOGGER.info("Recreated Yggdrasil service.");
                } else {
                    SocksProxyClient.LOGGER.warn("Failed to recreate Yggdrasil service.");
                }
            });
        });
    }
}
