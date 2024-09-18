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
    SocksProxyClientConfigEntry<String> proxyHost;
    SocksProxyClientConfigEntry<Integer> proxyPort;
    SocksProxyClientConfigEntry<String> proxyUsername;
    SocksProxyClientConfigEntry<String> proxyPassword;

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

        proxyHost = entryField("proxyHost", String.class);
        Option<String> yaclProxyHost = Option.<String>createBuilder()
                .name(proxyHost.getEntryTranslateKey())
                .available(useProxy.getValue())
                .binding(proxyHost.getDefaultValue(), proxyHost::getValue, proxyHost::setValue)
                .controller(ValidStringControllerBuilder::create)
                .build();

        proxyPort = entryField("proxyPort", Integer.class);
        Option<Integer> yaclProxyPort = Option.<Integer>createBuilder()
                .name(proxyPort.getEntryTranslateKey())
                .available(useProxy.getValue())
                .binding(proxyPort.getDefaultValue(), proxyPort::getValue, proxyPort::setValue)
                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1).max(65535).formatValue(value -> Text.literal(String.format("%d", value))))
                .build();

        proxyUsername = entryField("proxyUsername", String.class);
        Option<String> yaclProxyUsername = Option.<String>createBuilder()
                .name(proxyUsername.getEntryTranslateKey())
                .available(useProxy.getValue())
                .binding(proxyUsername.getDefaultValue(), proxyUsername::getValue, proxyUsername::setValue)
                .controller(StringControllerBuilder::create)
                .build();

        proxyPassword = entryField("proxyPassword", String.class);
        Option<String> yaclProxyPassword = Option.<String>createBuilder()
                .name(proxyPassword.getEntryTranslateKey())
                .available(useProxy.getValue() && socksVersion.getValue().equals(SocksVersion.SOCKS5))
                .binding(proxyPassword.getDefaultValue(), proxyPassword::getValue, proxyPassword::setValue)
                .controller(CredentialsStringControllerBuilder::create)
                .build();

        yaclSocksVersion.addListener((opt, v) -> yaclProxyPassword.setAvailable(v.equals(SocksVersion.SOCKS5) && useProxy.getValue()));

        groupBuilder.option(yaclSocksVersion);
        groupBuilder.option(yaclProxyHost);
        groupBuilder.option(yaclProxyPort);
        groupBuilder.option(yaclProxyUsername);
        groupBuilder.option(yaclProxyPassword);

        yaclUseProxy.addListener((opt, v) -> {
            yaclSocksVersion.setAvailable(v);
            yaclProxyHost.setAvailable(v);
            yaclProxyPort.setAvailable(v);
            yaclProxyUsername.setAvailable(v);
            yaclProxyPassword.setAvailable(v);
        });

        categoryBuilder.group(groupBuilder.build());

        return categoryBuilder.build();
    }
}
