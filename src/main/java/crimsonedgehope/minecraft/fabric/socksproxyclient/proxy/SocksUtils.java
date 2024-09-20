package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeys;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SocksUtils {
    public static void testReachability() {
        testReachability0("https://api.mojang.com");
        testReachability0("https://ipinfo.io");
    }

    public static void testReachability0(final String target) {
        final CompletableFuture<Pair<Boolean, Throwable>> test = CompletableFuture.supplyAsync(() -> {
            try {
                URL url = URI.create(target).toURL();
                final Proxy httpProxy = HttpProxyServerUtils.getProxyObject(true);
                final Proxy minecraftProxy = ServerConfig.getProxyForMinecraft(true);
                if (httpProxy.equals(Proxy.NO_PROXY) || minecraftProxy.equals(Proxy.NO_PROXY)) {
                    SocksProxyClientConfig.LOGGER.warn("No proxy to test.");
                    return new Pair<>(true, null);
                }

                final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(httpProxy);
                SocksProxyClientConfig.LOGGER.info("Testing connection to {} via proxy {}", target, minecraftProxy.address());
                MinecraftClient.getInstance().submit(() -> {
                    SystemToast.show(MinecraftClient.getInstance().getToastManager(),
                            SystemToast.Type.TUTORIAL_HINT,
                            Text.translatable(TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_TESTING),
                            Text.literal(target));
                });
                urlConnection.setConnectTimeout(com.mojang.authlib.minecraft.client.MinecraftClient.CONNECT_TIMEOUT_MS);
                urlConnection.setReadTimeout(com.mojang.authlib.minecraft.client.MinecraftClient.READ_TIMEOUT_MS);
                int res = urlConnection.getResponseCode();

                if (res != -1) {
                    if (res == 200) {
                        SocksProxyClientConfig.LOGGER.info("{} responded with {}", target, res);
                    } else {
                        SocksProxyClientConfig.LOGGER.warn("{} responded with non-200: {}", target, res);
                    }
                    final InputStream inputStream = urlConnection.getInputStream();
                    final String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    final Gson gson = new Gson();
                    final JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
                    SocksProxyClientConfig.LOGGER.info("{} response: {}", target, jsonObject.toString());
                    urlConnection.disconnect();
                } else {
                    SocksProxyClientConfig.LOGGER.warn("{} is not responding.", target);
                }
            } catch (JsonSyntaxException e) {
                return new Pair<>(false, new RuntimeException(target + " sent back no json.", e));
            } catch (IOException e) {
                return new Pair<>(false, new RuntimeException("IO failure!!", e));
            }
            return new Pair<>(true, null);
        });
        final CompletableFuture<Void> res = test.thenApplyAsync(v -> {
            MinecraftClient.getInstance().submit(() -> {
                MinecraftClient.getInstance().getToastManager().add(
                        new SystemToast(SystemToast.Type.TUTORIAL_HINT,
                                Text.translatable(v.getLeft()
                                        ? TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_TEST_SUCCESS
                                        : TranslateKeys.SOCKSPROXYCLIENT_CONFIG_GENERAL_PROXY_TEST_FAILURE
                                ), Text.literal(target)));
            });
            if (v.getLeft()) {
                return null;
            }
            Throwable t = v.getRight();
            SocksProxyClientConfig.LOGGER.error("", t);
            if (Objects.nonNull(t) && !(t instanceof JsonSyntaxException)) {
                SocksProxyClientConfig.LOGGER.error("Test not successful.", t);
            }
            return null;
        });
    }
}
