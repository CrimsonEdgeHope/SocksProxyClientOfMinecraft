package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.ProxyEntry;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.gui.YACLScreen;
import org.apache.logging.log4j.util.TriConsumer;

public interface ProxyEntryControllerBuilder extends ControllerBuilder<ProxyEntry> {
    static ProxyEntryControllerBuilderImpl create(Option<ProxyEntry> option) {
        return new ProxyEntryControllerBuilderImpl(option);
    }

    ProxyEntryControllerBuilder action(TriConsumer<YACLScreen, ProxyEntry, Runnable> action);
}
