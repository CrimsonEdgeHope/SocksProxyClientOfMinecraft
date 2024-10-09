package crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.Credential;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.Socks;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Proxy;
import java.net.SocketAddress;

@Environment(EnvType.CLIENT)
@Getter
public final class ProxyEntry {
    private final Proxy proxy;
    private final Socks version;

    @NotNull
    private final Credential credential;

    public ProxyEntry(Socks version, SocketAddress sa) {
        this(version, sa, null, null);
    }

    public ProxyEntry(Socks version, SocketAddress sa, @Nullable String username, @Nullable String password) {
        this(version, sa, new Credential(username, password));
    }

    public ProxyEntry(Socks version, SocketAddress sa, @NotNull Credential credential) {
        this.proxy = new Proxy(Proxy.Type.SOCKS, sa);
        this.version = version;
        this.credential = credential;
    }
}
