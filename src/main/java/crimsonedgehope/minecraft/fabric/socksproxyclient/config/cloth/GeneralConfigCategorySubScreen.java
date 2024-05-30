package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksVersion;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpToSocksServer;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.Requirement;
import net.minecraft.text.Text;

import java.util.Optional;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils.categoryField;

final class GeneralConfigCategorySubScreen extends ClothCategorySubScreen<GeneralConfig> {
    ClothConfigEntry<Boolean> useProxy;
    ClothConfigEntry<SocksVersion> socksVersion;
    ClothConfigEntry<String> customProxyHost;
    ClothConfigEntry<Integer> customProxyPort;
    ClothConfigEntry<String> customProxyUsername;
    ClothConfigEntry<String> customProxyPassword;

    public GeneralConfigCategorySubScreen(ClothAccess clothAccess) throws Exception {
        super(clothAccess, GeneralConfig.class);

        useProxy = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("useProxy", Boolean.class)) {
            @Override
            protected AbstractConfigListEntry<Boolean> buildClothConfigEntry() {
                return this.getBuilder().startBooleanToggle(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(v -> {
                            this.getConfigEntry().setValue(v);
                            if (v) {
                                HttpToSocksServer.INSTANCE.fire();
                            } else {
                                HttpToSocksServer.INSTANCE.cease();
                            }
                        }).build();
            }
        };

        socksVersion = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("socksVersion", SocksVersion.class)) {
            @Override
            protected AbstractConfigListEntry<SocksVersion> buildClothConfigEntry() {
                return this.getBuilder().startEnumSelector(
                                this.getConfigEntry().getTranslatableText(),
                                (Class<SocksVersion>) this.getConfigEntry().getDefaultValue().getClass(),
                                this.getConfigEntry().getValue()
                        )
                        .setRequirement(Requirement.isTrue(useProxy.getClothConfigEntry()))
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };

        customProxyHost = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("customProxyHost", String.class)) {
            @Override
            protected AbstractConfigListEntry<String> buildClothConfigEntry() {
                return this.getBuilder().startStrField(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setRequirement(Requirement.isTrue(useProxy.getClothConfigEntry()))
                        .setErrorSupplier(s -> {
                            Optional<Text> r = Optional.of(
                                    TranslateKeyUtil.itemAsText(this.getConfigEntry().getTranslateKey(), "error")
                            );
                            if (s.isEmpty()) {
                                return r;
                            }
                            return Optional.empty();
                        })
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };

        customProxyPort = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("customProxyPort", Integer.class)) {
            @Override
            protected AbstractConfigListEntry<Integer> buildClothConfigEntry() {
                return this.getBuilder().startIntField(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setRequirement(Requirement.isTrue(useProxy.getClothConfigEntry()))
                        .setErrorSupplier(v -> {
                            Optional<Text> r = Optional.of(
                                    TranslateKeyUtil.itemAsText(this.getConfigEntry().getTranslateKey(), "error")
                            );
                            if (v <= 0 || v >= 65536) {
                                return r;
                            }
                            return Optional.empty();
                        })
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(this.getConfigEntry()::setValue)
                        .build();
            }
        };

        customProxyUsername = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("customProxyUsername", String.class)) {
            @Override
            protected AbstractConfigListEntry<String> buildClothConfigEntry() {
                return this.getBuilder().startStrField(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setRequirement(Requirement.isTrue(useProxy.getClothConfigEntry()))
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(v -> {
                            this.getConfigEntry().setValue(v);
                            GeneralConfig.setCustomCredentialUsername(v);
                        })
                        .build();
            }
        };

        customProxyPassword = new ClothConfigEntry<>(clothAccess.configEntryBuilder(), entryField("customProxyPassword", String.class)) {
            @Override
            protected AbstractConfigListEntry<String> buildClothConfigEntry() {
                return this.getBuilder().startStrField(
                                this.getConfigEntry().getTranslatableText(),
                                this.getConfigEntry().getValue()
                        )
                        .setRequirement(Requirement.all(
                                Requirement.isTrue(useProxy.getClothConfigEntry()),
                                Requirement.isValue(socksVersion.getClothConfigEntry(), SocksVersion.SOCKS5)
                        ))
                        .setDefaultValue(this.getConfigEntry().getDefaultValue())
                        .setSaveConsumer(v -> {
                            this.getConfigEntry().setValue(v);
                            GeneralConfig.setCustomCredentialPassword(v);
                        })
                        .build();
            }
        };
    }

    private ConfigCategory buildCategory0(ClothAccess cloth) throws Exception {
        ConfigCategory generalCategory =
                cloth.configCategory(TranslateKeyUtil.configItemAsText(categoryField(this.configClass)));

        generalCategory.addEntry(useProxy.getClothConfigEntry());
        generalCategory.addEntry(socksVersion.getClothConfigEntry());
        generalCategory.addEntry(customProxyHost.getClothConfigEntry());
        generalCategory.addEntry(customProxyPort.getClothConfigEntry());
        generalCategory.addEntry(customProxyUsername.getClothConfigEntry());
        generalCategory.addEntry(customProxyPassword.getClothConfigEntry());

        return generalCategory;
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
