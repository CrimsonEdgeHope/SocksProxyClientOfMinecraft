package crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.Credential;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.Socks;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Getter
@Setter
public final class ProxyEntry {
    private Proxy proxy;
    private Socks version;

    @NotNull
    private Credential credential;

    public ProxyEntry(Socks version, InetSocketAddress sa) {
        this(version, sa, null, null);
    }

    public ProxyEntry(Socks version, InetSocketAddress sa, @Nullable String username, @Nullable String password) {
        this(version, sa, new Credential(username, password));
    }

    public ProxyEntry(Socks version, InetSocketAddress sa, @NotNull Credential credential) {
        this.proxy = new Proxy(Proxy.Type.SOCKS, sa);
        this.version = version;
        this.credential = credential;
    }
}
