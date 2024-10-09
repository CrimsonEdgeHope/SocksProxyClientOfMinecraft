package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.Proxy;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpProxyUtils {

    public static Proxy getProxyObject() {
        return getProxyObject(true);
    }

    public static Proxy getProxyObject(boolean useProxy) {
        SocksProxyClient.logger("HttpProxy").debug("getProxyObject: {}", useProxy);
        if (!useProxy || !HttpProxy.INSTANCE.isFired()) {
            return Proxy.NO_PROXY;
        } else {
            return new Proxy(Proxy.Type.HTTP, HttpProxy.INSTANCE.getChannel().localAddress());
        }
    }

}
