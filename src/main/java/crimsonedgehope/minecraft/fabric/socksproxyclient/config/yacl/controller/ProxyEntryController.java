package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.controller;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.ProxyEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;

public class ProxyEntryController implements Controller<ProxyEntry> {
    private final Option<ProxyEntry> option;
    private final BiConsumer<YACLScreen, ProxyEntry> action;
    private final Text text;

    public ProxyEntryController(Option<ProxyEntry> option, BiConsumer<YACLScreen, ProxyEntry> action) {
        this.option = option;
        this.action = action;
        this.text = Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_EDIT);
    }

    @Override
    public Option<ProxyEntry> option() {
        return option;
    }

    public BiConsumer<YACLScreen, ProxyEntry> action() {
        return action;
    }

    @Override
    public Text formatValue() {
        return text;
    }

    public ProxyEntry getEntry() {
        return option().pendingValue();
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new ProxyEntryControllerElement(this, screen, widgetDimension);
    }

    public static class ProxyEntryControllerElement extends ControllerWidget<ProxyEntryController> {
        private final String buttonString;

        public ProxyEntryControllerElement(ProxyEntryController control, YACLScreen screen, Dimension<Integer> dim) {
            super(control, screen, dim);
            buttonString = control.formatValue().getString().toLowerCase();
        }

        public void executeAction() {
            playDownSound();
            control.action().accept(screen, control.getEntry());
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isMouseOver(mouseX, mouseY) && isAvailable()) {
                executeAction();
                return true;
            }
            return false;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!focused) {
                return false;
            }

            if (keyCode == InputUtil.GLFW_KEY_ENTER || keyCode == InputUtil.GLFW_KEY_SPACE || keyCode == InputUtil.GLFW_KEY_KP_ENTER) {
                executeAction();
                return true;
            }

            return false;
        }

        @Override
        protected int getHoveredControlWidth() {
            return getUnhoveredControlWidth();
        }

        @Override
        public boolean canReset() {
            return false;
        }

        @Override
        public boolean matchesSearch(String query) {
            return super.matchesSearch(query) || buttonString.contains(query);
        }
    }

    @ApiStatus.Internal
    public static ProxyEntryController createInternal(Option<ProxyEntry> option, BiConsumer<YACLScreen, ProxyEntry> action) {
        return new ProxyEntryController(option, action);
    }
}
