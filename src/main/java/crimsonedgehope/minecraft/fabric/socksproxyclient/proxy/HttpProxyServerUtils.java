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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpProxyServerUtils {

    public static Proxy getProxyObject() {
        SocksProxyClient.LOGGER.debug("getProxyObject");
        if (!HttpToSocksServer.INSTANCE.isFired()) {
            return Proxy.NO_PROXY;
        } else {
            return new Proxy(Proxy.Type.HTTP, HttpToSocksServer.INSTANCE.getChannel().localAddress());
        }
    }

    public static void recreateAuthenticationService() {
        if (((MinecraftClientAccessor) MinecraftClient.getInstance()).getUserApiService().equals(UserApiService.OFFLINE)
                && GeneralConfig.usingProxy()) {
            HttpProxyServerUtils.createAuthenticationService();
        }
    }

    public static void createAuthenticationService() {
        SocksProxyClient.LOGGER.debug("recreateAuthenticationService");
        MinecraftClient client = MinecraftClient.getInstance();
        RunArgs args = MinecraftClientMixinVariables.getRunArgs();
        MinecraftClientAccessor accessor = ((MinecraftClientAccessor) client);
        accessor.setAuthenticationService(new YggdrasilAuthenticationService(getProxyObject()));
        accessor.setSessionService(accessor.getAuthenticationService().createMinecraftSessionService());
        accessor.setUserApiService(accessor.invokeCreateUserApiService(accessor.getAuthenticationService(), args));
        accessor.setSkinProvider(new PlayerSkinProvider(accessor.getTextureManager(), new File(args.directories.assetDir, "skins"), accessor.getSessionService()));
        accessor.setSocialInteractionsManager(new SocialInteractionsManager(client, accessor.getUserApiService()));
        accessor.setTelemetryManager(new TelemetryManager(client, accessor.getUserApiService(), args.network.session));
        accessor.setProfileKeys(ProfileKeys.create(accessor.getUserApiService(), args.network.session, client.runDirectory.toPath()));
        accessor.setAbuseReportContext(AbuseReportContext.create(ReporterEnvironment.ofIntegratedServer(), accessor.getUserApiService()));
    }
}
