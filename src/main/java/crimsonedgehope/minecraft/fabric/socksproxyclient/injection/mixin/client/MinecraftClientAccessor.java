package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.client;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor
    MinecraftSessionService getSessionService();
    @Accessor("sessionService") @Mutable
    void setSessionService(MinecraftSessionService service);

    @Accessor("field_26902")
    SocialInteractionsService getSocialInteractionService();
    @Accessor("field_26902") @Mutable
    void setSocialInteractionsService(SocialInteractionsService service);

    @Invoker("method_31382")
    SocialInteractionsService invokeCreateSocialInteractionsService(YggdrasilAuthenticationService service, RunArgs args);

    @Accessor
    TextureManager getTextureManager();

    @Accessor("skinProvider") @Mutable
    void setSkinProvider(PlayerSkinProvider provider);

    @Accessor("socialInteractionsManager") @Mutable
    void setSocialInteractionsManager(SocialInteractionsManager manager);
}
