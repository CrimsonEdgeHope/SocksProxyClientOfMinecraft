package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfigEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksVersion;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpToSocksServer;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import net.minecraft.text.Text;

import java.util.Optional;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils.categoryField;

public final class GeneralConfigCategorySubScreen extends CategorySubScreen<GeneralConfig> {
    public GeneralConfigCategorySubScreen() {
        super(GeneralConfig.class);
    }

    private ConfigCategory buildCategory0(ClothAccess cloth) throws Exception {
        ConfigEntryBuilder entryBuilder = cloth.configEntryBuilder();
        ConfigCategory generalCategory =
                cloth.configCategory(TranslateKeyUtil.configItemAsText(categoryField(this.configClass)));

        SocksProxyClientConfigEntry<Boolean> useProxy = entryField("useProxy", Boolean.class);
        BooleanListEntry useProxyEntry = entryBuilder.startBooleanToggle(
                        useProxy.getTranslatableText(),
                        useProxy.getValue()
                )
                .setDefaultValue(useProxy.getDefaultValue())
                .setSaveConsumer(v -> {
                    useProxy.setValue(v);
                    if (v) {
                        HttpToSocksServer.INSTANCE.fire();
                    } else {
                        HttpToSocksServer.INSTANCE.cease();
                    }
                })
                .build();
        generalCategory.addEntry(useProxyEntry);

        SocksProxyClientConfigEntry<SocksVersion> socksVersion = entryField("socksVersion", SocksVersion.class);
        EnumListEntry<SocksVersion> socksVersionEntry = entryBuilder.startEnumSelector(
                        socksVersion.getTranslatableText(),
                        (Class<SocksVersion>) socksVersion.getDefaultValue().getClass(),
                        socksVersion.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxyEntry))
                .setDefaultValue(socksVersion.getDefaultValue())
                .setSaveConsumer(socksVersion::setValue)
                .build();
        generalCategory.addEntry(socksVersionEntry);

        SocksProxyClientConfigEntry<String> customProxyHost = entryField("customProxyHost", String.class);
        StringListEntry customProxyHostEntry = entryBuilder.startStrField(
                        customProxyHost.getTranslatableText(),
                        customProxyHost.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxyEntry))
                .setErrorSupplier(s -> {
                    Optional<Text> r = Optional.of(
                            TranslateKeyUtil.itemAsText(customProxyHost.getTranslateKey(), "error")
                    );
                    if (s.isEmpty()) {
                        return r;
                    }
                    return Optional.empty();
                })
                .setDefaultValue(customProxyHost.getDefaultValue())
                .setSaveConsumer(customProxyHost::setValue)
                .build();
        generalCategory.addEntry(customProxyHostEntry);

        SocksProxyClientConfigEntry<Integer> customProxyPort = entryField("customProxyPort", Integer.class);
        IntegerListEntry customProxyPortEntry = entryBuilder.startIntField(
                        customProxyPort.getTranslatableText(),
                        customProxyPort.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxyEntry))
                .setErrorSupplier(v -> {
                    Optional<Text> r = Optional.of(
                            TranslateKeyUtil.itemAsText(customProxyPort.getTranslateKey(), "error")
                    );
                    if (v <= 0 || v >= 65536) {
                        return r;
                    }
                    return Optional.empty();
                })
                .setDefaultValue(customProxyPort.getDefaultValue())
                .setSaveConsumer(customProxyPort::setValue)
                .build();
        generalCategory.addEntry(customProxyPortEntry);

        SocksProxyClientConfigEntry<String> customProxyUsername = entryField("customProxyUsername", String.class);
        StringListEntry customProxyUsernameEntry = entryBuilder.startStrField(
                        customProxyUsername.getTranslatableText(),
                        customProxyUsername.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxyEntry))
                .setDefaultValue(customProxyUsername.getDefaultValue())
                .setSaveConsumer(v -> {
                    customProxyUsername.setValue(v);
                    GeneralConfig.setCustomCredentialUsername(v);
                })
                .build();
        generalCategory.addEntry(customProxyUsernameEntry);

        SocksProxyClientConfigEntry<String> customProxyPassword = entryField("customProxyPassword", String.class);
        StringListEntry customProxyPasswordEntry = entryBuilder.startStrField(
                        customProxyPassword.getTranslatableText(),
                        customProxyPassword.getValue()
                )
                .setRequirement(Requirement.all(
                        Requirement.isTrue(useProxyEntry),
                        Requirement.isValue(socksVersionEntry, SocksVersion.SOCKS5)
                ))
                .setDefaultValue(customProxyPassword.getDefaultValue())
                .setSaveConsumer(v -> {
                    customProxyPassword.setValue(v);
                    GeneralConfig.setCustomCredentialPassword(v);
                })
                .build();
        generalCategory.addEntry(customProxyPasswordEntry);

        return generalCategory;
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
