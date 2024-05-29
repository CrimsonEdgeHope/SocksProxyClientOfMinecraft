package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin_variables;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.RunArgs;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MinecraftClientMixinVariables {
    @Getter @Setter
    private static RunArgs runArgs;
}
