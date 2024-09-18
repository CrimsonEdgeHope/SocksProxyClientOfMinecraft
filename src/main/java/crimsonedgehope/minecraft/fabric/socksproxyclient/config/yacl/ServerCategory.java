package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfigEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller.ValidStringControllerBuilder;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DNSOverHTTPSProvider;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import net.minecraft.text.Text;

final class ServerCategory extends YACLCategory<ServerConfig> {

    SocksProxyClientConfigEntry<Boolean> proxyMinecraft;
    SocksProxyClientConfigEntry<Boolean> minecraftRemoteResolve;
    SocksProxyClientConfigEntry<Boolean> minecraftRemoteResolveDismissSystemHosts;
    SocksProxyClientConfigEntry<DNSOverHTTPSProvider> minecraftRemoteResolveProvider;
    SocksProxyClientConfigEntry<String> customMinecraftRemoteResolveProvider;

    SocksProxyClientConfigEntry<Boolean> proxyYggdrasil;
    SocksProxyClientConfigEntry<Boolean> proxyPlayerSkinDownload;
    SocksProxyClientConfigEntry<Boolean> proxyServerResourceDownload;
    SocksProxyClientConfigEntry<Boolean> proxyBlockListSupplier;
    SocksProxyClientConfigEntry<Boolean> httpRemoteResolve;

    SocksProxyClientConfigEntry<Boolean> imposeProxyOnMinecraftLoopback;

    ServerCategory(YACLAccess yacl) {
        super(yacl, ServerConfig.class);
    }

    @Override
    public ConfigCategory buildConfigCategory() throws Exception {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder();

        categoryBuilder.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER))
                .tooltip(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_TOOLTIP));

        proxyMinecraft = entryField("proxyMinecraft", Boolean.class);
        Option<Boolean> yaclProxyMinecraft = Option.<Boolean>createBuilder()
                .name(proxyMinecraft.getEntryTranslateKey())
                .description(OptionDescription.of(proxyMinecraft.getDescriptionTranslateKey()))
                .binding(proxyMinecraft.getDefaultValue(), proxyMinecraft::getValue, proxyMinecraft::setValue)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                .build();
        categoryBuilder.option(yaclProxyMinecraft);

        OptionGroup.Builder groupMinecraftServerDomainName = OptionGroup.createBuilder();
        groupMinecraftServerDomainName.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_MINECRAFTDOMAINNAMERESOLUTION));

        minecraftRemoteResolve = entryField("minecraftRemoteResolve", Boolean.class);
        Option<Boolean> yaclMinecraftRemoteResolve = Option.<Boolean>createBuilder()
                .name(minecraftRemoteResolve.getEntryTranslateKey())
                .description(OptionDescription.of(minecraftRemoteResolve.getDescriptionTranslateKey()))
                .binding(minecraftRemoteResolve.getDefaultValue(), minecraftRemoteResolve::getValue, minecraftRemoteResolve::setValue)
                .available(proxyMinecraft.getValue())
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                .build();

        minecraftRemoteResolveDismissSystemHosts = entryField("minecraftRemoteResolveDismissSystemHosts", Boolean.class);
        Option<Boolean> yaclMinecraftRemoteResolveDismissSystemHosts = Option.<Boolean>createBuilder()
                .name(minecraftRemoteResolveDismissSystemHosts.getEntryTranslateKey())
                .description(OptionDescription.of(minecraftRemoteResolveDismissSystemHosts.getDescriptionTranslateKey()))
                .binding(minecraftRemoteResolveDismissSystemHosts.getDefaultValue(), minecraftRemoteResolveDismissSystemHosts::getValue, minecraftRemoteResolveDismissSystemHosts::setValue)
                .available(proxyMinecraft.getValue())
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                .build();

        minecraftRemoteResolveProvider = entryField("minecraftRemoteResolveProvider", DNSOverHTTPSProvider.class);
        Option<DNSOverHTTPSProvider> yaclMinecraftRemoteResolveProvider = Option.<DNSOverHTTPSProvider>createBuilder()
                .name(minecraftRemoteResolveProvider.getEntryTranslateKey())
                .binding(minecraftRemoteResolveProvider.getDefaultValue(), minecraftRemoteResolveProvider::getValue, minecraftRemoteResolveProvider::setValue)
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(DNSOverHTTPSProvider.class).formatValue(v -> Text.literal(v.displayName)))
                .available(proxyMinecraft.getValue())
                .build();

        customMinecraftRemoteResolveProvider = entryField("customMinecraftRemoteResolveProvider", String.class);
        Option<String> yaclCustomMinecraftRemoteResolveProvider = Option.<String>createBuilder()
                .name(customMinecraftRemoteResolveProvider.getEntryTranslateKey())
                .binding(customMinecraftRemoteResolveProvider.getDefaultValue(), customMinecraftRemoteResolveProvider::getValue, customMinecraftRemoteResolveProvider::setValue)
                .controller(opt -> ValidStringControllerBuilder.create(opt).validityPredication(s -> s.startsWith("https://")))
                .available(proxyMinecraft.getValue())
                .build();

        groupMinecraftServerDomainName.option(yaclMinecraftRemoteResolve);
        groupMinecraftServerDomainName.option(yaclMinecraftRemoteResolveDismissSystemHosts);
        groupMinecraftServerDomainName.option(yaclMinecraftRemoteResolveProvider);
        groupMinecraftServerDomainName.option(yaclCustomMinecraftRemoteResolveProvider);

        yaclProxyMinecraft.addListener((opt, v) -> {
            yaclMinecraftRemoteResolve.setAvailable(v);
            yaclMinecraftRemoteResolveDismissSystemHosts.setAvailable(v);
            yaclMinecraftRemoteResolveProvider.setAvailable(v);
            yaclCustomMinecraftRemoteResolveProvider.setAvailable(v);
        });

        categoryBuilder.group(groupMinecraftServerDomainName.build());

        OptionGroup.Builder groupServices = OptionGroup.createBuilder();
        groupServices.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_SERVICES));

        proxyYggdrasil = entryField("proxyYggdrasil", Boolean.class);
        Option<Boolean> yaclProxyYggdrasil = Option.<Boolean>createBuilder()
                .name(proxyYggdrasil.getEntryTranslateKey())
                .description(OptionDescription.of(proxyYggdrasil.getDescriptionTranslateKey()))
                .binding(proxyYggdrasil.getDefaultValue(), proxyYggdrasil::getValue, proxyYggdrasil::setValue)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                .build();
        proxyPlayerSkinDownload = entryField("proxyPlayerSkinDownload", Boolean.class);
        Option<Boolean> yaclProxyPlayerSkinDownload = Option.<Boolean>createBuilder()
                .name(proxyPlayerSkinDownload.getEntryTranslateKey())
                .description(OptionDescription.of(proxyPlayerSkinDownload.getDescriptionTranslateKey()))
                .binding(proxyPlayerSkinDownload.getDefaultValue(), proxyPlayerSkinDownload::getValue, proxyPlayerSkinDownload::setValue)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                .build();
        proxyServerResourceDownload = entryField("proxyServerResourceDownload", Boolean.class);
        Option<Boolean> yaclProxyServerResourceDownload = Option.<Boolean>createBuilder()
                .name(proxyServerResourceDownload.getEntryTranslateKey())
                .description(OptionDescription.of(proxyServerResourceDownload.getDescriptionTranslateKey()))
                .binding(proxyServerResourceDownload.getDefaultValue(), proxyServerResourceDownload::getValue, proxyServerResourceDownload::setValue)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                .build();
        proxyBlockListSupplier = entryField("proxyBlockListSupplier", Boolean.class);
        Option<Boolean> yaclProxyBlockListSupplier = Option.<Boolean>createBuilder()
                .name(proxyBlockListSupplier.getEntryTranslateKey())
                .description(OptionDescription.of(proxyBlockListSupplier.getDescriptionTranslateKey()))
                .binding(proxyBlockListSupplier.getDefaultValue(), proxyBlockListSupplier::getValue, proxyBlockListSupplier::setValue)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                .build();
        httpRemoteResolve = entryField("httpRemoteResolve", Boolean.class);
        Option<Boolean> yaclHttpRemoteResolve = Option.<Boolean>createBuilder()
                .name(httpRemoteResolve.getEntryTranslateKey())
                .description(OptionDescription.of(httpRemoteResolve.getDescriptionTranslateKey()))
                .binding(httpRemoteResolve.getDefaultValue(), httpRemoteResolve::getValue, httpRemoteResolve::setValue)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                .build();

        groupServices.option(yaclProxyYggdrasil)
                .option(yaclProxyPlayerSkinDownload)
                .option(yaclProxyServerResourceDownload)
                .option(yaclProxyBlockListSupplier)
                .option(yaclHttpRemoteResolve);

        categoryBuilder.group(groupServices.build());

        OptionGroup.Builder groupAdvanced = OptionGroup.createBuilder().collapsed(true);
        groupAdvanced.name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_SERVER_ADVANCED));

        imposeProxyOnMinecraftLoopback = entryField("imposeProxyOnMinecraftLoopback", Boolean.class);
        Option<Boolean> yaclImposeProxyOnMinecraftLoopback = Option.<Boolean>createBuilder()
                .name(imposeProxyOnMinecraftLoopback.getEntryTranslateKey())
                .description(OptionDescription.of(imposeProxyOnMinecraftLoopback.getDescriptionTranslateKey()))
                .binding(imposeProxyOnMinecraftLoopback.getDefaultValue(), imposeProxyOnMinecraftLoopback::getValue, imposeProxyOnMinecraftLoopback::setValue)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                .build();
        groupAdvanced.option(yaclImposeProxyOnMinecraftLoopback);

        categoryBuilder.group(groupAdvanced.build());

        return categoryBuilder.build();
    }
}
