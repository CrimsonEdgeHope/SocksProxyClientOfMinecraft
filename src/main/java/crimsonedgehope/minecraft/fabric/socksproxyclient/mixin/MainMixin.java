package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ProxyConfig;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(Main.class)
public class MainMixin {
    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/main/Main;getOption(Ljoptsimple/OptionSet;Ljoptsimple/OptionSpec;)Ljava/lang/Object;",
                    shift = At.Shift.AFTER),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Ljava/net/Proxy;equals(Ljava/lang/Object;)Z",
                            shift = At.Shift.BEFORE)),
            method = "main",
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    /*
     Proxy credential from game param.
     */
    private static void injected(String[] args, CallbackInfo ci, @Local OptionParser optionParser) {
        OptionSpec<String> proxyUserOption = optionParser.accepts("proxyUser").withRequiredArg().defaultsTo("");
        OptionSpec<String> proxyPassOption = optionParser.accepts("proxyPass").withRequiredArg().defaultsTo("");
        OptionSet optionSet = optionParser.parse(args);

        String proxyUser = optionSet.valueOf(proxyUserOption);
        String proxyPass = optionSet.valueOf(proxyPassOption);

        ProxyConfig.setCredential(proxyUser, proxyPass);
        ProxyConfig.setCredentialFromGameParam(proxyUser, proxyPass);

        SocksProxyClient.logger().debug(String.format("proxyUser: %s", proxyUser));
        SocksProxyClient.logger().debug(String.format("proxyPass: %s", proxyPass.isEmpty() ? "Not set" : "******"));
    }
}
