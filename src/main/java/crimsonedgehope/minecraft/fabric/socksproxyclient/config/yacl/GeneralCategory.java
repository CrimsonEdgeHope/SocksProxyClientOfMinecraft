package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfigEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller.CredentialsStringControllerBuilder;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller.ValidStringControllerBuilder;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.SocksVersion;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.text.Text;

final class GeneralCategory extends YACLCategory<GeneralConfig> {

    SocksProxyClientConfigEntry<Boolean> useProxy;

    SocksProxyClientConfigEntry<SocksVersion> socksVersion;
    SocksProxyClientConfigEntry<String> customProxyHost;
    SocksProxyClientConfigEntry<Integer> customProxyPort;
    SocksProxyClientConfigEntry<String> customProxyUsername;
    SocksProxyClientConfigEntry<String> customProxyPassword;

    GeneralCategory(YACLAccess yacl) {
        super(yacl, GeneralConfig.class);
    }

    @Override
    public ConfigCategory buildConfigCategory() throws Exception {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder();

        categoryBuilder.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL))
                .tooltip(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_TOOLTIP));

        useProxy = entryField("useProxy", Boolean.class);
        Option<Boolean> yaclUseProxy = Option.<Boolean>createBuilder()
                .name(useProxy.getEntryTranslateKey())
                .binding(useProxy.getDefaultValue(), useProxy::getValue, useProxy::setValue)
                .controller(BooleanControllerBuilder::create)
                .build();
        categoryBuilder.option(yaclUseProxy);

        OptionGroup.Builder groupBuilder = OptionGroup.createBuilder()
                .name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY));

        socksVersion = entryField("socksVersion", SocksVersion.class);
        Option<SocksVersion> yaclSocksVersion = Option.<SocksVersion>createBuilder()
                .name(socksVersion.getEntryTranslateKey())
                .available(useProxy.getValue())
                .binding(socksVersion.getDefaultValue(), socksVersion::getValue, socksVersion::setValue)
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(SocksVersion.class))
                .build();

        customProxyHost = entryField("customProxyHost", String.class);
        Option<String> yaclCustomProxyHost = Option.<String>createBuilder()
                .name(customProxyHost.getEntryTranslateKey())
                .available(useProxy.getValue())
                .binding(customProxyHost.getDefaultValue(), customProxyHost::getValue, customProxyHost::setValue)
                .controller(ValidStringControllerBuilder::create)
                .build();

        customProxyPort = entryField("customProxyPort", Integer.class);
        Option<Integer> yaclCustomProxyPort = Option.<Integer>createBuilder()
                .name(customProxyPort.getEntryTranslateKey())
                .available(useProxy.getValue())
                .binding(customProxyPort.getDefaultValue(), customProxyPort::getValue, customProxyPort::setValue)
                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1).max(65535).formatValue(value -> Text.literal(String.format("%d", value))))
                .build();

        customProxyUsername = entryField("customProxyUsername", String.class);
        Option<String> yaclCustomProxyUsername = Option.<String>createBuilder()
                .name(customProxyUsername.getEntryTranslateKey())
                .available(useProxy.getValue())
                .binding(customProxyUsername.getDefaultValue(), customProxyUsername::getValue, customProxyUsername::setValue)
                .controller(StringControllerBuilder::create)
                .build();

        customProxyPassword = entryField("customProxyPassword", String.class);
        Option<String> yaclCustomProxyPassword = Option.<String>createBuilder()
                .name(customProxyPassword.getEntryTranslateKey())
                .available(useProxy.getValue() && socksVersion.getValue().equals(SocksVersion.SOCKS5))
                .binding(customProxyPassword.getDefaultValue(), customProxyPassword::getValue, customProxyPassword::setValue)
                .controller(CredentialsStringControllerBuilder::create)
                .build();

        yaclSocksVersion.addListener((opt, v) -> yaclCustomProxyPassword.setAvailable(v.equals(SocksVersion.SOCKS5) && useProxy.getValue()));

        groupBuilder.option(yaclSocksVersion);
        groupBuilder.option(yaclCustomProxyHost);
        groupBuilder.option(yaclCustomProxyPort);
        groupBuilder.option(yaclCustomProxyUsername);
        groupBuilder.option(yaclCustomProxyPassword);

        yaclUseProxy.addListener((opt, v) -> {
            yaclSocksVersion.setAvailable(v);
            yaclCustomProxyHost.setAvailable(v);
            yaclCustomProxyPort.setAvailable(v);
            yaclCustomProxyUsername.setAvailable(v);
            yaclCustomProxyPassword.setAvailable(v);
        });

        categoryBuilder.group(groupBuilder.build());

        return categoryBuilder.build();
    }
}
