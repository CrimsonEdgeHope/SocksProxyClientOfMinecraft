package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.report.AbuseReportContext;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.ProfileKeys;
import net.minecraft.client.util.telemetry.TelemetryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor
    YggdrasilAuthenticationService getAuthenticationService();
    @Accessor("authenticationService") @Mutable
    void setAuthenticationService(YggdrasilAuthenticationService service);

    @Accessor
    MinecraftSessionService getSessionService();
    @Accessor("sessionService") @Mutable
    void setSessionService(MinecraftSessionService service);

    @Accessor
    UserApiService getUserApiService();
    @Accessor("userApiService") @Mutable
    void setUserApiService(UserApiService service);

    @Invoker("createUserApiService")
    UserApiService invokeCreateUserApiService(YggdrasilAuthenticationService service, RunArgs args);

    @Accessor
    TextureManager getTextureManager();

    @Accessor("skinProvider") @Mutable
    void setSkinProvider(PlayerSkinProvider provider);

    @Accessor("socialInteractionsManager") @Mutable
    void setSocialInteractionsManager(SocialInteractionsManager manager);

    @Accessor("telemetryManager") @Mutable
    void setTelemetryManager(TelemetryManager manager);

    @Accessor("profileKeys") @Mutable
    void setProfileKeys(ProfileKeys keys);

    @Accessor("abuseReportContext")
    void setAbuseReportContext(AbuseReportContext context);
}
