package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.mixin.MinecraftClientAccessor;
import crimsonedgehope.minecraft.fabric.socksproxyclient.mixin_variables.MinecraftClientMixinVariables;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.report.AbuseReportContext;
import net.minecraft.client.report.ReporterEnvironment;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.ProfileKeys;
import net.minecraft.client.util.telemetry.TelemetryManager;

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

    public static CompletableFuture<Void> recreateAuthenticationService() {
        if (((MinecraftClientAccessor) MinecraftClient.getInstance()).getUserApiService().equals(UserApiService.OFFLINE)
                && GeneralConfig.usingProxy()) {
            return HttpProxyServerUtils.createAuthenticationService();
        }
        return CompletableFuture.runAsync(() -> {});
    }

    public static CompletableFuture<Void> createAuthenticationService() {
        final MinecraftClient client = MinecraftClient.getInstance();
        return client.submit(() -> {
            SocksProxyClient.LOGGER.debug("recreateAuthenticationService");
            SocksProxyClient.LOGGER.info("Attempt to recreate authentication service");
            HttpToSocksServer.INSTANCE.cease();
            HttpToSocksServer.INSTANCE.fire();
            RunArgs args = MinecraftClientMixinVariables.getRunArgs();
            MinecraftClientAccessor accessor = ((MinecraftClientAccessor) client);
            accessor.setAuthenticationService(new YggdrasilAuthenticationService(Proxy.NO_PROXY));
            accessor.setSessionService(accessor.getAuthenticationService().createMinecraftSessionService());
            accessor.setUserApiService(accessor.invokeCreateUserApiService(accessor.getAuthenticationService(), args));
            accessor.setSkinProvider(new PlayerSkinProvider(accessor.getTextureManager(), new File(args.directories.assetDir, "skins"), accessor.getSessionService()));
            accessor.setSocialInteractionsManager(new SocialInteractionsManager(client, accessor.getUserApiService()));
            accessor.setTelemetryManager(new TelemetryManager(client, accessor.getUserApiService(), args.network.session));
            accessor.setProfileKeys(ProfileKeys.create(accessor.getUserApiService(), args.network.session, client.runDirectory.toPath()));
            accessor.setAbuseReportContext(AbuseReportContext.create(ReporterEnvironment.ofIntegratedServer(), accessor.getUserApiService()));
            SocksProxyClient.LOGGER.info("Recreated authentication service.");
        });
    }
}
