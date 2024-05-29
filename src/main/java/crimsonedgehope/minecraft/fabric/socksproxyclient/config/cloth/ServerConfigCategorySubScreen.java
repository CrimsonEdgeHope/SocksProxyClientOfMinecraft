package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfigEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils.categoryField;

public final class ServerConfigCategorySubScreen extends CategorySubScreen<ServerConfig> {

    public ServerConfigCategorySubScreen() {
        super(ServerConfig.class);
    }

    private ConfigCategory buildCategory0(ClothAccess cloth) throws Exception {
        ConfigEntryBuilder entryBuilder = cloth.configEntryBuilder();
        ConfigCategory serverCategory =
                cloth.configCategory(TranslateKeyUtil.configItemAsText(categoryField(this.configClass)));

        SocksProxyClientConfigEntry<Boolean> imposeProxyOnLoopback = entryField("imposeProxyOnLoopback", Boolean.class);
        BooleanListEntry imposeProxyOnLoopbackEntry = entryBuilder.startBooleanToggle(
                        imposeProxyOnLoopback.getTranslatableText(),
                        imposeProxyOnLoopback.getValue()
                )
                .setDefaultValue(imposeProxyOnLoopback.getDefaultValue())
                .setSaveConsumer(imposeProxyOnLoopback::setValue)
                .build();
        serverCategory.addEntry(imposeProxyOnLoopbackEntry);

        return serverCategory;
    }

    @Override
    public ConfigCategory buildCategory(ClothAccess cloth) {
        try {
            return buildCategory0(cloth);
        } catch (Exception e) {
            SocksProxyClient.LOGGER.error("Error building cloth category screen!", e);
            throw new RuntimeException(e);
        }
    }
}
