package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralProxyConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClothConfigScreen {

    private static ClothAccess cloth;

    public static Screen getScreen(Screen parentScreen) {
        cloth = new ClothAccess(parentScreen, Text.translatable(TranslateKeyUtil.configItem()));

        ConfigBuilder builder = cloth.getConfigBuilder();

        ConfigEntryBuilder entryBuilder = cloth.configEntryBuilder();
        ConfigCategory generalCategory =
                cloth.configCategory(Text.translatable(TranslateKeyUtil.configItem(GeneralProxyConfig.CATEGORY)));

        builder.setSavingRunnable(GeneralProxyConfig.INSTANCE::save);

        BooleanListEntry useProxy = entryBuilder.startBooleanToggle(
                        Text.translatable(GeneralProxyConfig.useProxy.getTranslateKey()),
                        GeneralProxyConfig.useProxy.getValue()
                )
                .setDefaultValue(GeneralProxyConfig.useProxy.getDefaultValue())
                .setSaveConsumer(GeneralProxyConfig.useProxy::setValue)
                .build();
        generalCategory.addEntry(useProxy);

        EnumListEntry useProxyFrom = entryBuilder.startEnumSelector(
                        Text.translatable(GeneralProxyConfig.useProxyFrom.getTranslateKey()),
                        GeneralProxyConfig.SocksSelection.class,
                        GeneralProxyConfig.useProxyFrom.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxy))
                .setDefaultValue(GeneralProxyConfig.useProxyFrom.getDefaultValue())
                .setSaveConsumer(GeneralProxyConfig.useProxyFrom::setValue)
                .build();
        generalCategory.addEntry(useProxyFrom);

        EnumListEntry socksVersion = entryBuilder.startEnumSelector(
                        Text.translatable(GeneralProxyConfig.socksVersion.getTranslateKey()),
                        GeneralProxyConfig.SocksVersion.class,
                        GeneralProxyConfig.socksVersion.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxy))
                .setDefaultValue(GeneralProxyConfig.socksVersion.getDefaultValue())
                .setSaveConsumer(GeneralProxyConfig.socksVersion::setValue)
                .build();
        generalCategory.addEntry(socksVersion);

        StringListEntry customProxyHost = entryBuilder.startStrField(
                        Text.translatable(GeneralProxyConfig.customProxyHost.getTranslateKey()),
                        GeneralProxyConfig.customProxyHost.getValue()
                )
                .setRequirement(Requirement.all(
                        Requirement.isTrue(useProxy),
                        Requirement.isValue(useProxyFrom, GeneralProxyConfig.SocksSelection.CUSTOM)
                ))
                .setErrorSupplier(s -> {
                    Optional<Text> r = Optional.of(Text.empty());
                    if (s.isEmpty()) {
                        return r;
                    }
                    return Optional.empty();
                })
                .setDefaultValue(GeneralProxyConfig.customProxyHost.getDefaultValue())
                .setSaveConsumer(GeneralProxyConfig.customProxyHost::setValue)
                .build();
        generalCategory.addEntry(customProxyHost);

        IntegerListEntry customProxyPort = entryBuilder.startIntField(
                        Text.translatable(GeneralProxyConfig.customProxyPort.getTranslateKey()),
                        GeneralProxyConfig.customProxyPort.getValue()
                )
                .setRequirement(Requirement.all(
                        Requirement.isTrue(useProxy),
                        Requirement.isValue(useProxyFrom, GeneralProxyConfig.SocksSelection.CUSTOM)
                ))
                .setErrorSupplier(v -> {
                    if (v <= 0 || v >= 65536) {
                        return Optional.of(Text.empty());
                    }
                    return Optional.empty();
                })
                .setDefaultValue(GeneralProxyConfig.customProxyPort.getDefaultValue())
                .setSaveConsumer(GeneralProxyConfig.customProxyPort::setValue)
                .build();
        generalCategory.addEntry(customProxyPort);

        StringListEntry customProxyUsername = entryBuilder.startStrField(
                        Text.translatable(GeneralProxyConfig.customProxyUsername.getTranslateKey()),
                        GeneralProxyConfig.customProxyUsername.getValue()
                )
                .setRequirement(Requirement.all(
                        Requirement.isTrue(useProxy),
                        Requirement.isValue(useProxyFrom, GeneralProxyConfig.SocksSelection.CUSTOM)
                ))
                .setDefaultValue(GeneralProxyConfig.customProxyUsername.getDefaultValue())
                .setSaveConsumer(v -> {
                    GeneralProxyConfig.customProxyUsername.setValue(v);
                    GeneralProxyConfig.setCustomCredentialUsername(v);
                })
                .build();
        generalCategory.addEntry(customProxyUsername);

        StringListEntry customProxyPassword = entryBuilder.startStrField(
                        Text.translatable(GeneralProxyConfig.customProxyPassword.getTranslateKey()),
                        GeneralProxyConfig.customProxyPassword.getValue()
                )
                .setRequirement(Requirement.all(
                        Requirement.isTrue(useProxy),
                        Requirement.isValue(useProxyFrom, GeneralProxyConfig.SocksSelection.CUSTOM),
                        Requirement.isValue(socksVersion, GeneralProxyConfig.SocksVersion.SOCKS5)
                ))
                .setDefaultValue(GeneralProxyConfig.customProxyPassword.getDefaultValue())
                .setSaveConsumer(v -> {
                    GeneralProxyConfig.customProxyPassword.setValue(v);
                    GeneralProxyConfig.setCustomCredentialPassword(v);
                })
                .build();
        generalCategory.addEntry(customProxyPassword);

        BooleanListEntry imposeProxyOnLoopback = entryBuilder.startBooleanToggle(
                        Text.translatable(GeneralProxyConfig.imposeProxyOnLoopback.getTranslateKey()),
                        GeneralProxyConfig.imposeProxyOnLoopback.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxy))
                .setDefaultValue(GeneralProxyConfig.imposeProxyOnLoopback.getDefaultValue())
                .setSaveConsumer(GeneralProxyConfig.imposeProxyOnLoopback::setValue)
                .build();
        generalCategory.addEntry(imposeProxyOnLoopback);

        builder.setTransparentBackground(true);
        builder.setAlwaysShowTabs(true);
        builder.setShouldTabsSmoothScroll(true);
        return builder.build();
    }
}
