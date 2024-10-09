package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.dns;

import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.HttpProxyUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.xbill.DNS.DohResolver;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Environment(EnvType.CLIENT)
@Mixin(DohResolver.class)
public class DohResolverMixin {
    @Inject(
            method = "lambda$getHttpClient$0",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/reflect/Method;invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            ),
            remap = false,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void injected(Executor key, CallbackInfoReturnable<Object> cir, Object httpClientBuilder) {
        HttpClient.Builder builder = (HttpClient.Builder) httpClientBuilder;
        builder.proxy(new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                List<Proxy> res = new ArrayList<>();
                res.add(HttpProxyUtils.getProxyObject());
                return res;
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

            }
        });
    }

    @Redirect(
            method = "sendAndGetMessageBytes",
            at = @At(value = "INVOKE", target = "Ljava/net/URL;openConnection()Ljava/net/URLConnection;"),
            remap = false
    )
    private URLConnection redirected(URL instance) throws IOException {
        return instance.openConnection(HttpProxyUtils.getProxyObject());
    }
}
