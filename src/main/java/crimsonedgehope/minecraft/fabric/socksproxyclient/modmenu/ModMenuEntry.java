package crimsonedgehope.minecraft.fabric.socksproxyclient.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth.ClothConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ModMenuEntry implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            try {
                return ClothConfigScreen.getScreen(parent);
            } catch (Exception e) {
                SocksProxyClient.LOGGER.error("Error opening config screen from modmenu!", e);
                throw new RuntimeException(e);
            }
        };
    }
}
