package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.text.Text;

import java.util.Optional;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils.categoryField;

final class ServerConfigCategorySubScreen extends ClothCategorySubScreen<ServerConfig> {

    final GeneralConfigCategorySubScreen generalConfigCategorySubScreen;

    ClothConfigEntry<Boolean> proxyMinecraft;

    ClothConfigEntry<Boolean> proxyYggdrasilAuth;
    ClothConfigEntry<Boolean> proxyPlayerSkinDownload;
    ClothConfigEntry<Boolean> proxyServerResourceDownload;
    ClothConfigEntry<Boolean> proxyBlockListSupplier;
    ClothConfigEntry<Boolean> httpRemoteResolve;

    ClothConfigEntry<Boolean> imposeProxyOnMinecraftLoopback;

    public ServerConfigCategorySubScreen(
            ClothAccess clothAccess,
            GeneralConfigCategorySubScreen generalConfigCategorySubScreen
    ) throws Exception {
        super(clothAccess, ServerConfig.class);
        this.generalConfigCategorySubScreen = generalConfigCategorySubScreen;

        proxyMinecraft = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("proxyMinecraft", Boolean.class)) {
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

        proxyYggdrasilAuth = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("proxyYggdrasilAuth", Boolean.class)) {
            @Override
            protected AbstractConfigListEntry<Boolean> buildClothConfigEntry() {
                return this.getBuilder().startBooleanToggle(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setTooltip(Optional.of(this.getConfigEntry().getDescriptionTranslatableText().toArray(Text[]::new)))
                        .setRequirement(Requirement.isTrue(generalConfigCategorySubScreen.useProxy.getClothConfigEntry()))
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };

        proxyPlayerSkinDownload = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("proxyPlayerSkinDownload", Boolean.class)) {
            @Override
            protected AbstractConfigListEntry<Boolean> buildClothConfigEntry() {
                return this.getBuilder().startBooleanToggle(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setTooltip(Optional.of(this.getConfigEntry().getDescriptionTranslatableText().toArray(Text[]::new)))
                        .setRequirement(Requirement.isTrue(generalConfigCategorySubScreen.useProxy.getClothConfigEntry()))
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };

        proxyServerResourceDownload = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("proxyServerResourceDownload", Boolean.class)) {
            @Override
            protected AbstractConfigListEntry<Boolean> buildClothConfigEntry() {
                return this.getBuilder().startBooleanToggle(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setTooltip(Optional.of(this.getConfigEntry().getDescriptionTranslatableText().toArray(Text[]::new)))
                        .setRequirement(Requirement.isTrue(generalConfigCategorySubScreen.useProxy.getClothConfigEntry()))
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };

        proxyBlockListSupplier = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("proxyBlockListSupplier", Boolean.class)) {
            @Override
            protected AbstractConfigListEntry<Boolean> buildClothConfigEntry() {
                return this.getBuilder().startBooleanToggle(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setTooltip(Optional.of(this.getConfigEntry().getDescriptionTranslatableText().toArray(Text[]::new)))
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

        imposeProxyOnMinecraftLoopback = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("imposeProxyOnMinecraftLoopback", Boolean.class)) {
            @Override
            protected AbstractConfigListEntry<Boolean> buildClothConfigEntry() {
                return this.getBuilder().startBooleanToggle(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setTooltip(Optional.of(this.getConfigEntry().getDescriptionTranslatableText().toArray(Text[]::new)))
                        .setRequirement(
                                Requirement.all(
                                        Requirement.isTrue(generalConfigCategorySubScreen.useProxy.getClothConfigEntry()),
                                        Requirement.isTrue(proxyMinecraft.getClothConfigEntry())
                                )
                        )
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };
    }

    private ConfigCategory buildCategory0(ClothAccess cloth) throws Exception {
        String transKey = TranslateKeyUtil.configItem(categoryField(this.configClass));
        Text text = Text.translatable(transKey);

        ConfigCategory serverCategory = cloth.configCategory(text);

        serverCategory.addEntry(proxyMinecraft.getClothConfigEntry());
        serverCategory.addEntry(proxyYggdrasilAuth.getClothConfigEntry());
        serverCategory.addEntry(proxyPlayerSkinDownload.getClothConfigEntry());
        serverCategory.addEntry(proxyServerResourceDownload.getClothConfigEntry());
        serverCategory.addEntry(proxyBlockListSupplier.getClothConfigEntry());
        serverCategory.addEntry(httpRemoteResolve.getClothConfigEntry());

        SubCategoryBuilder subAdvancedBuilder = clothAccess.configEntryBuilder().startSubCategory(TranslateKeyUtil.itemAsText(transKey, "advanced"));
        subAdvancedBuilder.setExpanded(false);

        subAdvancedBuilder.add(imposeProxyOnMinecraftLoopback.getClothConfigEntry());
        serverCategory.addEntry(subAdvancedBuilder.build());

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
