package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.socks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
@Environment(EnvType.CLIENT)
public class SocksProxyCredential {
    @Nullable private String username;
    @Nullable private String password;
}
