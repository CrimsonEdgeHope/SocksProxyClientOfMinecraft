package crimsonedgehope.minecraft.fabric.socksproxyclient.modmenu;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth.ClothConfigScreen;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ModMenuEntry implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            try {
                return ClothConfigScreen.getScreen(parent);
            } catch (Throwable e) {
                return null;
            }
        };
    }
}
