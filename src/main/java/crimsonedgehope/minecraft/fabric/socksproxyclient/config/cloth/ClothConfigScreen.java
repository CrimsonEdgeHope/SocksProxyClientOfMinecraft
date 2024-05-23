package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralProxyConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpToSocksServer;
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
        cloth = new ClothAccess(parentScreen, TranslateKeyUtil.configItemAsText());

        ConfigBuilder builder = cloth.getConfigBuilder();

        ConfigEntryBuilder entryBuilder = cloth.configEntryBuilder();
        ConfigCategory generalCategory =
                cloth.configCategory(TranslateKeyUtil.configItemAsText(GeneralProxyConfig.CATEGORY));

        builder.setSavingRunnable(GeneralProxyConfig.INSTANCE::save);

        BooleanListEntry useProxy = entryBuilder.startBooleanToggle(
                        GeneralProxyConfig.useProxy.getTranslatableText(),
                        GeneralProxyConfig.useProxy.getValue()
                )
                .setDefaultValue(GeneralProxyConfig.useProxy.getDefaultValue())
                .setSaveConsumer(v -> {
                    GeneralProxyConfig.useProxy.setValue(v);
                    if (v) {
                        HttpToSocksServer.INSTANCE.fire();
                    } else {
                        HttpToSocksServer.INSTANCE.cease();
                    }
                })
                .build();
        generalCategory.addEntry(useProxy);

        EnumListEntry socksVersion = entryBuilder.startEnumSelector(
                        GeneralProxyConfig.socksVersion.getTranslatableText(),
                        GeneralProxyConfig.SocksVersion.class,
                        GeneralProxyConfig.socksVersion.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxy))
                .setDefaultValue(GeneralProxyConfig.socksVersion.getDefaultValue())
                .setSaveConsumer(GeneralProxyConfig.socksVersion::setValue)
                .build();
        generalCategory.addEntry(socksVersion);

        StringListEntry customProxyHost = entryBuilder.startStrField(
                        GeneralProxyConfig.customProxyHost.getTranslatableText(),
                        GeneralProxyConfig.customProxyHost.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxy))
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
                        GeneralProxyConfig.customProxyPort.getTranslatableText(),
                        GeneralProxyConfig.customProxyPort.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxy))
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
                        GeneralProxyConfig.customProxyUsername.getTranslatableText(),
                        GeneralProxyConfig.customProxyUsername.getValue()
                )
                .setRequirement(Requirement.isTrue(useProxy))
                .setDefaultValue(GeneralProxyConfig.customProxyUsername.getDefaultValue())
                .setSaveConsumer(v -> {
                    GeneralProxyConfig.customProxyUsername.setValue(v);
                    GeneralProxyConfig.setCustomCredentialUsername(v);
                })
                .build();
        generalCategory.addEntry(customProxyUsername);

        StringListEntry customProxyPassword = entryBuilder.startStrField(
                        GeneralProxyConfig.customProxyPassword.getTranslatableText(),
                        GeneralProxyConfig.customProxyPassword.getValue()
                )
                .setRequirement(Requirement.all(
                        Requirement.isTrue(useProxy),
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
                        GeneralProxyConfig.imposeProxyOnLoopback.getTranslatableText(),
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
