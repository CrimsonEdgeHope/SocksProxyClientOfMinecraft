package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.screen;

import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IConnectScreenMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ConnectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(ConnectScreen.class)
public class ConnectScreenMixin implements IConnectScreenMixin {
    @Unique
    private String host;

    @Override
    public void socksProxyClient$setHost(String host) {
        this.host = host;
    }

    @Override
    public String socksProxyClient$getHost() {
        return this.host;
    }
}
