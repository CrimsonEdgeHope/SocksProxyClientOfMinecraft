package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.ProxyEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.SocksProxyClientConfigEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller.ProxyEntryControllerBuilder;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.screen.ProxyEntryEditScreen;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.socks.SocksUtils;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

final class GeneralCategory extends YACLCategory<GeneralConfig> {

    SocksProxyClientConfigEntry<Boolean> useProxy;
    SocksProxyClientConfigEntry<List> proxies;

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
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .flag(OptionFlag.GAME_RESTART)
                .build();
        categoryBuilder.option(yaclUseProxy);

        OptionGroup.Builder proxyGroupBuilder = OptionGroup.createBuilder()
                .name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY));

        proxies = entryField("proxies", List.class);
        ListOption<ProxyEntry> yaclProxies = ListOption.<ProxyEntry>createBuilder()
                .name(proxies.getEntryTranslateKey())
                .description(OptionDescription.of(proxies.getDescriptionTranslateKey()))
                .initial((ProxyEntry) proxies.getDefaultValue().get(0))
                .binding((List<ProxyEntry>) proxies.getDefaultValue(), proxies::getValue, proxies::setValue)
                .collapsed(false)
                .controller(opt -> ProxyEntryControllerBuilder.create((Option<ProxyEntry>) opt).action((screen, entry, callback) -> {
                    MinecraftClient.getInstance().setScreen(new ProxyEntryEditScreen(screen, entry, callback));
                }))
                .insertEntriesAtEnd(true)
                .flag(OptionFlag.GAME_RESTART)
                .available(useProxy.getValue())
                .build();

        categoryBuilder.group(yaclProxies);

        ButtonOption yaclTestReachability = ButtonOption.createBuilder()
                .name(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_TEST))
                .description(OptionDescription.of(Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_TEST_TOOLTIP)))
                .available(true)
                .action((screen, opt) -> SocksUtils.testReachability())
                .available(useProxy.getValue())
                .build();

        proxyGroupBuilder.option(yaclTestReachability);

        yaclUseProxy.addListener((opt, v) -> {
            yaclProxies.setAvailable(v);
            yaclTestReachability.setAvailable(v);
        });

        categoryBuilder.group(proxyGroupBuilder.build());

        return categoryBuilder.build();
    }
}
