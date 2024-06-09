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
import net.minecraft.client.report.AbuseReportContext;
import net.minecraft.client.report.ReporterEnvironment;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.ProfileKeys;
import net.minecraft.client.util.telemetry.TelemetryManager;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MinecraftUtils {
    public static CompletableFuture<Void> recreateYggdrasilService() {
        return createYggdrasilService();
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
                    accessor.setSkinProvider(new PlayerSkinProvider(accessor.getTextureManager(), new File(args.directories.assetDir, "skins"), accessor.getSessionService()));
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
