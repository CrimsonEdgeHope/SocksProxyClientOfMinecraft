package crimsonedgehope.minecraft.fabric.socksproxyclient.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.YACLConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ModMenuEntry implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            try {
                return YACLConfigScreen.getScreen(parent);
            } catch (Throwable e) {
                SocksProxyClient.logger("ModMenu Entry").error("Cannot initialize ModMenu config screen entry!!", e);
                return null;
            }
        };
    }
}
