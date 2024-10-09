package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.ProxyEntry;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.gui.YACLScreen;

import java.util.function.BiConsumer;

public class ProxyEntryControllerBuilderImpl implements ProxyEntryControllerBuilder {
    private final Option<ProxyEntry> option;
    private BiConsumer<YACLScreen, ProxyEntry> action;

    protected ProxyEntryControllerBuilderImpl(Option<ProxyEntry> option) {
        this.option = option;
    }

    @Override
    public ProxyEntryControllerBuilder action(BiConsumer<YACLScreen, ProxyEntry> action) {
        this.action = action;
        return this;
    }

    @Override
    public Controller<ProxyEntry> build() {
        return ProxyEntryController.createInternal(option, action);
    }
}
