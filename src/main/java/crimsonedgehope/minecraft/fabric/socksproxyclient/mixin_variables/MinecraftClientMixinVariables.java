package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin_variables;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.RunArgs;

@Environment(EnvType.CLIENT)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MinecraftClientMixinVariables {
    @Getter @Setter
    private static RunArgs runArgs;
}
