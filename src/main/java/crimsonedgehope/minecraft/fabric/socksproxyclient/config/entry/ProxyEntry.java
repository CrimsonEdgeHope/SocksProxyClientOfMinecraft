package crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.socks.SocksProxyCredential;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.socks.SocksVersion;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Getter
public class ProxyEntry {
    private Proxy proxy;
    @NotNull @Setter
    private SocksVersion version;

    @NotNull @Setter
    private SocksProxyCredential socksProxyCredential;

    public ProxyEntry(@NotNull SocksVersion version, InetSocketAddress sa) {
        this(version, sa, null, null);
    }

    public ProxyEntry(@NotNull SocksVersion version, InetSocketAddress sa, @Nullable String username, @Nullable String password) {
        this(version, sa, new SocksProxyCredential(username, password));
    }

    public ProxyEntry(@NotNull SocksVersion version, InetSocketAddress sa, @NotNull SocksProxyCredential socksProxyCredential) {
        this.proxy = new Proxy(Proxy.Type.SOCKS, sa);
        this.version = version;
        this.socksProxyCredential = socksProxyCredential;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
        Validate.isTrue(proxy.type().equals(Proxy.Type.SOCKS));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProxyEntry entry)) {
            return false;
        }
        if (this.getVersion() != entry.getVersion()) {
            return false;
        }
        Proxy proxy0 = this.getProxy();
        Proxy proxy1 = entry.getProxy();
        if (!proxy0.type().equals(proxy1.type())) {
            return false;
        }

        InetSocketAddress sa0 = (InetSocketAddress) proxy0.address();
        InetSocketAddress sa1 = (InetSocketAddress) proxy1.address();

        if (!compare(sa0.getHostString(), sa1.getHostString())) {
            return false;
        }
        if (sa0.getPort() != sa1.getPort()) {
            return false;
        }

        SocksProxyCredential c0 = this.getSocksProxyCredential();
        SocksProxyCredential c1 = entry.getSocksProxyCredential();

        if (!compare(c0.getUsername(), c1.getUsername())) {
            return false;
        }
        if (!compare(c0.getPassword(), c1.getPassword())) {
            return false;
        }
        return true;
    }

    private boolean compare(String s1, String s2) {
        if (Objects.nonNull(s1) && Objects.nonNull(s2)) {
            if (!s1.equals(s2)) {
                return false;
            }
        } else if (!(Objects.isNull(s1) && Objects.isNull(s2))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = getProxy().hashCode();
        result = 31 * result + getVersion().hashCode();
        result = 31 * result + getSocksProxyCredential().hashCode();
        return result;
    }
}
