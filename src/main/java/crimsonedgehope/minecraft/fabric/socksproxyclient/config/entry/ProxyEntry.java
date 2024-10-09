package crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.Credential;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.SocksVersion;
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
public class ProxyEntry {
    private Proxy proxy;
    @NotNull @Setter
    private SocksVersion version;

    @NotNull @Setter
    private Credential credential;

    public ProxyEntry(@NotNull SocksVersion version, InetSocketAddress sa) {
        this(version, sa, null, null);
    }

    public ProxyEntry(@NotNull SocksVersion version, InetSocketAddress sa, @Nullable String username, @Nullable String password) {
        this(version, sa, new Credential(username, password));
    }

    public ProxyEntry(@NotNull SocksVersion version, InetSocketAddress sa, @NotNull Credential credential) {
        this.proxy = new Proxy(Proxy.Type.SOCKS, sa);
        this.version = version;
        this.credential = credential;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
        if (!proxy.type().equals(Proxy.Type.SOCKS)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProxyEntry entry)) {
            return false;
        }
        if (!getProxy().equals(entry.getProxy())) {
            return false;
        }
        if (getVersion() != entry.getVersion()) {
            return false;
        }
        return getCredential().equals(entry.getCredential());
    }

    @Override
    public int hashCode() {
        int result = getProxy().hashCode();
        result = 31 * result + getVersion().hashCode();
        result = 31 * result + getCredential().hashCode();
        return result;
    }
}
