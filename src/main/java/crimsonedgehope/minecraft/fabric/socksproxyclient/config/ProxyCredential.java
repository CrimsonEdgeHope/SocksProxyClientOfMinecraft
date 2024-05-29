package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class ProxyCredential {
    @Nullable private String username;
    @Nullable private String password;
}
