package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class Credential {
    @Nullable private String username;
    @Nullable private String password;
}
