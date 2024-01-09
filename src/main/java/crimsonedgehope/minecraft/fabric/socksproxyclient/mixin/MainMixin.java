package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {
    @Inject(at = @At(value = "INVOKE",
            target = "Ljava/net/Proxy;equals(Ljava/lang/Object;)Z",
            shift = At.Shift.BEFORE), method = "main")
    private static void injected(String[] args, CallbackInfo ci) {
        OptionParser optionParser = new OptionParser();
        optionParser.allowsUnrecognizedOptions();
        OptionSpec<String> proxyUser = optionParser.accepts("proxyUser").withRequiredArg().defaultsTo("");
        OptionSpec<String> proxyPass = optionParser.accepts("proxyPass").withRequiredArg().defaultsTo("");
        OptionSet optionSet = optionParser.parse(args);
        SocksProxyClient.Auth.setUsername(optionSet.valueOf(proxyUser));
        SocksProxyClient.Auth.setPassword(optionSet.valueOf(proxyPass));
    }
}
