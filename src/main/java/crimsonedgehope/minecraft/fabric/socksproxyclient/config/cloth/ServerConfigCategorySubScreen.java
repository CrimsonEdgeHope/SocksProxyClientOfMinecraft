package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DNSOverHTTPSProvider;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.text.Text;

import java.net.URL;
import java.util.Optional;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils.categoryField;

final class ServerConfigCategorySubScreen extends ClothCategorySubScreen<ServerConfig> {

    final GeneralConfigCategorySubScreen generalConfigCategorySubScreen;

    ClothConfigEntry<Boolean> proxyMinecraft;
    ClothConfigEntry<Boolean> minecraftRemoteResolve;
    ClothConfigEntry<Boolean> minecraftRemoteResolveDismissSystemHosts;
    ClothConfigEntry<DNSOverHTTPSProvider> minecraftRemoteResolveProvider;
    ClothConfigEntry<String> customMinecraftRemoteResolveProvider;

    ClothConfigEntry<Boolean> proxyYggdrasil;
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

        minecraftRemoteResolve = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("minecraftRemoteResolve", Boolean.class)) {
            @Override
            protected AbstractConfigListEntry<Boolean> buildClothConfigEntry() {
                return this.getBuilder().startBooleanToggle(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setRequirement(
                                Requirement.all(
                                        Requirement.isTrue(generalConfigCategorySubScreen.useProxy.getClothConfigEntry()),
                                        Requirement.isTrue(proxyMinecraft.getClothConfigEntry())
                                )
                        )
                        .setTooltip(Optional.of(this.getConfigEntry().getDescriptionTranslatableText().toArray(Text[]::new)))
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };

        minecraftRemoteResolveDismissSystemHosts = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("minecraftRemoteResolveDismissSystemHosts", Boolean.class)) {
            @Override
            protected AbstractConfigListEntry<Boolean> buildClothConfigEntry() {
                return this.getBuilder().startBooleanToggle(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setRequirement(
                                Requirement.all(
                                        Requirement.isTrue(generalConfigCategorySubScreen.useProxy.getClothConfigEntry()),
                                        Requirement.isTrue(proxyMinecraft.getClothConfigEntry()),
                                        Requirement.isTrue(minecraftRemoteResolve.getClothConfigEntry())
                                )
                        )
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };

        minecraftRemoteResolveProvider = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("minecraftRemoteResolveProvider", DNSOverHTTPSProvider.class)) {
            @Override
            protected AbstractConfigListEntry<DNSOverHTTPSProvider> buildClothConfigEntry() {
                return this.getBuilder().startEnumSelector(
                                this.getConfigEntry().getTranslatableText(),
                                (Class<DNSOverHTTPSProvider>) this.getConfigEntry().getDefaultValue().getClass(),
                                this.getConfigEntry().getValue()
                        )
                        .setEnumNameProvider(e -> Text.of(((DNSOverHTTPSProvider) e).displayName))
                        .setRequirement(
                                Requirement.all(
                                        Requirement.isTrue(generalConfigCategorySubScreen.useProxy.getClothConfigEntry()),
                                        Requirement.isTrue(proxyMinecraft.getClothConfigEntry()),
                                        Requirement.isTrue(minecraftRemoteResolve.getClothConfigEntry())
                                )
                        )
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };

        customMinecraftRemoteResolveProvider = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("customMinecraftRemoteResolveProvider", String.class)) {
            @Override
            protected AbstractConfigListEntry<String> buildClothConfigEntry() {
                AbstractConfigListEntry<String> r = this.getBuilder().startStrField(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setRequirement(
                                Requirement.all(
                                        Requirement.isTrue(generalConfigCategorySubScreen.useProxy.getClothConfigEntry()),
                                        Requirement.isTrue(proxyMinecraft.getClothConfigEntry()),
                                        Requirement.isTrue(minecraftRemoteResolve.getClothConfigEntry()),
                                        Requirement.isValue(minecraftRemoteResolveProvider.getClothConfigEntry(), DNSOverHTTPSProvider.CUSTOM)
                                )
                        )
                        .setErrorSupplier(s -> {
                            Optional<Text> err = Optional.of(
                                    TranslateKeyUtil.itemAsText(this.getConfigEntry().getTranslateKey(), "error")
                            );
                            if (s.isEmpty() || s.isBlank()) {
                                return err;
                            }
                            if (!s.startsWith("https://")) {
                                return err;
                            }
                            try {
                                new URL(s);
                            } catch (Exception e) {
                                return Optional.of(Text.of(e.getLocalizedMessage()));
                            }
                            return Optional.empty();
                        })
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
                r.setDisplayRequirement(r.getRequirement());
                return r;
            }
        };

        proxyYggdrasil = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("proxyYggdrasil", Boolean.class)) {
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

        SubCategoryBuilder subMcDomainBuilder = clothAccess.configEntryBuilder().startSubCategory(TranslateKeyUtil.itemAsText(transKey, "minecraftDomainName"));

        subMcDomainBuilder.add(minecraftRemoteResolve.getClothConfigEntry());
        subMcDomainBuilder.add(minecraftRemoteResolveDismissSystemHosts.getClothConfigEntry());

        SubCategoryBuilder subDOHBuilder = clothAccess.configEntryBuilder().startSubCategory(TranslateKeyUtil.itemAsText(transKey, "minecraftDomainName", "doh"));

        subDOHBuilder.add(minecraftRemoteResolveProvider.getClothConfigEntry());
        subDOHBuilder.add(customMinecraftRemoteResolveProvider.getClothConfigEntry());
        subDOHBuilder.setExpanded(true);

        subMcDomainBuilder.add(subDOHBuilder.build());
        subMcDomainBuilder.setExpanded(true);

        serverCategory.addEntry(subMcDomainBuilder.build());

        SubCategoryBuilder subServicesBuilder = clothAccess.configEntryBuilder().startSubCategory(TranslateKeyUtil.itemAsText(transKey, "services"));
        subServicesBuilder.setExpanded(true);

        subServicesBuilder.add(proxyYggdrasil.getClothConfigEntry());
        subServicesBuilder.add(proxyPlayerSkinDownload.getClothConfigEntry());
        subServicesBuilder.add(proxyServerResourceDownload.getClothConfigEntry());
        subServicesBuilder.add(proxyBlockListSupplier.getClothConfigEntry());
        subServicesBuilder.add(httpRemoteResolve.getClothConfigEntry());
        serverCategory.addEntry(subServicesBuilder.build());

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
