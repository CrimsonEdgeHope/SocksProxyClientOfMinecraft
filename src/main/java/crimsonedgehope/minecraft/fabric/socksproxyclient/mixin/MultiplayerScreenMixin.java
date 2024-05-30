package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth.ClothConfigScreen;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyServerUtils;
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
        adder.add(EmptyWidget.ofHeight(4));
        AxisGridWidget axisGridWidget3 = adder.add(new AxisGridWidget(308, 20, AxisGridWidget.DisplayAxis.HORIZONTAL));

        ButtonWidget recreateAuthButton = ButtonWidget.builder(
                TranslateKeyUtil.itemAsText(Arrays.asList("screen", "recreateAuth")),
                button -> {
                    button.active = false;
                    HttpProxyServerUtils.recreateAuthenticationService().thenRun(() -> button.active = true);
                }
        ).width(152).build();
        ((ScreenAccessor) this).invokeAddDrawableChild(recreateAuthButton);
        axisGridWidget3.add(recreateAuthButton);

        ButtonWidget openConfigScreenButton = ButtonWidget.builder(
                TranslateKeyUtil.itemAsText(Arrays.asList("screen", "config")),
                button -> {
                    try {
                        ((ScreenAccessor) this).getClient().setScreen(ClothConfigScreen.getScreen((MultiplayerScreen) (Object) this));
                    } catch (Exception e) {
                        button.active = false;
                    }
                }).width(152).build();
        ((ScreenAccessor) this).invokeAddDrawableChild(openConfigScreenButton);
        axisGridWidget3.add(openConfigScreenButton);
        try {
            Class.forName("me.shedaniel.clothconfig2.api.ValueHolder");
        } catch (Exception e) {
            openConfigScreenButton.active = false;
        }
    }
}
