package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.Requirement;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils.categoryField;

final class ServerConfigCategorySubScreen extends ClothCategorySubScreen<ServerConfig> {

    final GeneralConfigCategorySubScreen generalConfigCategorySubScreen;

    ClothConfigEntry<Boolean> imposeProxyOnLoopback;
    ClothConfigEntry<Boolean> httpRemoteResolve;

    public ServerConfigCategorySubScreen(
            ClothAccess clothAccess,
            GeneralConfigCategorySubScreen generalConfigCategorySubScreen
    ) throws Exception {
        super(clothAccess, ServerConfig.class);
        this.generalConfigCategorySubScreen = generalConfigCategorySubScreen;

        imposeProxyOnLoopback = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("imposeProxyOnLoopback", Boolean.class)) {
            @Override
            protected AbstractConfigListEntry<Boolean> buildClothConfigEntry() {
                return this.getBuilder().startBooleanToggle(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setRequirement(Requirement.isTrue(generalConfigCategorySubScreen.useProxy.getClothConfigEntry()))
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };

        httpRemoteResolve = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("httpRemoteResolve", Boolean.class)) {
            @Override
            protected AbstractConfigListEntry<Boolean> buildClothConfigEntry() {
                return this.getBuilder().startBooleanToggle(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setRequirement(Requirement.isTrue(generalConfigCategorySubScreen.useProxy.getClothConfigEntry()))
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };
    }

    private ConfigCategory buildCategory0(ClothAccess cloth) throws Exception {
        ConfigCategory serverCategory =
                cloth.configCategory(TranslateKeyUtil.configItemAsText(categoryField(this.configClass)));

        serverCategory.addEntry(imposeProxyOnLoopback.getClothConfigEntry());
        serverCategory.addEntry(httpRemoteResolve.getClothConfigEntry());

        return serverCategory;
    }

    @Override
    public ConfigCategory buildClothCategory() {
        try {
            return buildCategory0(this.clothAccess);
        } catch (Exception e) {
            SocksProxyClient.LOGGER.error("Error building cloth category screen!", e);
            throw new RuntimeException(e);
        }
    }
}
