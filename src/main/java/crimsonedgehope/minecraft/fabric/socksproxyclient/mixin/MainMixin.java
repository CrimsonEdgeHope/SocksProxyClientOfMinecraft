package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Main.class)
public class MainMixin {
    @Inject(method = "main",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/main/Main;getOption(Ljoptsimple/OptionSet;Ljoptsimple/OptionSpec;)Ljava/lang/Object;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE)
    )
    private static void injected(String[] args, CallbackInfo ci) throws Exception {
        SocksProxyClient.preInit();
    }
}
