package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyServerUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClothConfigScreen {

    private static ClothAccess cloth;

    public static Screen getScreen(Screen parentScreen) {
        cloth = new ClothAccess(parentScreen, TranslateKeyUtil.configItemAsText());

        new GeneralConfigCategorySubScreen().buildCategory(cloth);
        new ServerConfigCategorySubScreen().buildCategory(cloth);

        ConfigBuilder builder = cloth.getConfigBuilder();
        builder.setSavingRunnable(() -> {
            ClothUtils.saveAll();
            HttpProxyServerUtils.createAuthenticationService();
        });
        builder.setTransparentBackground(true)
                .setAlwaysShowTabs(true)
                .setShouldTabsSmoothScroll(true);
        return builder.build();
    }
}
