package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.screen;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth.ClothConfigScreen;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {
    @Inject(method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/GridWidget;refreshPositions()V",
                    shift = At.Shift.BEFORE
            ), locals = LocalCapture.CAPTURE_FAILHARD)
    private void injected(
            CallbackInfo ci,
            ButtonWidget buttonWidget,
            ButtonWidget buttonWidget2,
            ButtonWidget buttonWidget3,
            ButtonWidget buttonWidget4,
            GridWidget gridWidget,
            GridWidget.Adder adder,
            AxisGridWidget axisGridWidget,
            AxisGridWidget axisGridWidget2
    ) {
        if (!GeneralConfig.showButtonsInMultiplayerScreen()) {
            return;
        }
        adder.add(EmptyWidget.ofHeight(4));
        AxisGridWidget axisGridWidget3 = adder.add(new AxisGridWidget(308, 20, AxisGridWidget.DisplayAxis.HORIZONTAL));

        ButtonWidget openConfigScreenButton = ButtonWidget.builder(
                TranslateKeyUtil.itemAsText(Arrays.asList("screen", "config")),
                button -> {
                    try {
                        ((ScreenAccessor) this).getClient().setScreen(ClothConfigScreen.getScreen((MultiplayerScreen) (Object) this));
                    } catch (Exception e) {
                        SocksProxyClient.logger(this.getClass().getSimpleName()).error("Where's my config screen?", e);
                        button.active = false;
                    }
                }).width(308).build();
        ((ScreenAccessor) this).invokeAddDrawableChild(openConfigScreenButton);
        axisGridWidget3.add(openConfigScreenButton);
        try {
            Class.forName("me.shedaniel.clothconfig2.api.ValueHolder");
        } catch (Exception e) {
            openConfigScreenButton.active = false;
        }
    }
}
