package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import lombok.Getter;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

final class ClothAccess {
    @Getter
    private final Screen parentScreen;
    @Getter
    private final ConfigBuilder configBuilder;

    public ClothAccess(Screen parentScreen, Text title) {
        this.parentScreen = parentScreen;
        this.configBuilder = ConfigBuilder.create().setParentScreen(parentScreen).setTitle(title);
    }

    public ConfigCategory configCategory(Text title) {
        return configBuilder.getOrCreateCategory(title);
    }

    public ConfigEntryBuilder configEntryBuilder() {
        return configBuilder.entryBuilder();
    }
}
