package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor
    MinecraftClient getClient();

    @Invoker("addDrawableChild")
    <T extends Element & Drawable> T invokeAddDrawableChild(T drawableElement);
}
