package crimsonedgehope.minecraft.fabric.socksproxyclient.modmenu;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.UpdateChannel;
import com.terraformersmc.modmenu.api.UpdateChecker;
import com.terraformersmc.modmenu.api.UpdateInfo;
import com.terraformersmc.modmenu.util.HttpUtil;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.MiscellaneousConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl.YACLConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.impl.util.version.VersionParser;
import net.minecraft.SharedConstants;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public final class ModMenuEntry implements ModMenuApi {

    private static final String MODRINTH = "https://modrinth.com/mod/socksproxyclient";
    private static final String METADATA_URL = "https://garment.warpedinnether.top/socksproxyclient";
    private static final Logger LOGGER = SocksProxyClient.logger("UpdateChecker");

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            try {
                return YACLConfigScreen.getScreen(parent);
            } catch (Throwable e) {
                return null;
            }
        };
    }

    @Override
    public UpdateChecker getUpdateChecker() {
        return () -> {
            if (!MiscellaneousConfig.shouldCheckUpdates()) {
                return null;
            }
            try {
                HttpRequest.Builder request = HttpRequest.newBuilder().GET().uri(URI.create(METADATA_URL));
                HttpResponse<String> response = HttpUtil.request(request, HttpResponse.BodyHandlers.ofString());
                int status = response.statusCode();
                if (status != 200) {
                    throw new UnsupportedOperationException("Remote server returned status " + status + ". Expected 200.");
                }
                JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                JsonObject gameVersions = jsonObject.get("versions").getAsJsonObject();
                String gameVersion = SharedConstants.getGameVersion().getName();
                if (!gameVersions.has(gameVersion)) {
                    LOGGER.debug("No version found for {}", gameVersion);
                    return null;
                }
                JsonObject channels = gameVersions.getAsJsonObject(gameVersion);
                String modVersion = FabricLoader.getInstance().getModContainer("socksproxyclient").get().getMetadata().getVersion().getFriendlyString();
                SemanticVersion semantic = VersionParser.parseSemantic(modVersion);
                Optional<String> prereleaseKey = semantic.getPrereleaseKey();
                String releaseChannel = prereleaseKey.orElse("release");
                if (!channels.has(releaseChannel)) {
                    LOGGER.debug("No release channel found for {}!", releaseChannel);
                    return null;
                }
                JsonArray channel = channels.get(releaseChannel).getAsJsonArray();
                for (JsonElement element : channel.asList()) {
                    String v = element.getAsString();
                    LOGGER.debug("{} {}", v, semantic.getFriendlyString());
                    if (VersionParser.parseSemantic(v).compareTo(semantic) < 0) {
                        return new UpdateInfo() {
                            @Override
                            public boolean isUpdateAvailable() {
                                return true;
                            }

                            @Override
                            public String getDownloadLink() {
                                return MODRINTH;
                            }

                            @Override
                            public UpdateChannel getUpdateChannel() {
                                return switch (releaseChannel) {
                                    case "release" -> UpdateChannel.RELEASE;
                                    case "alpha" -> UpdateChannel.ALPHA;
                                    case "beta" -> UpdateChannel.BETA;
                                    default -> throw new IllegalStateException("Unexpected value: " + releaseChannel);
                                };
                            }
                        };
                    }
                }
                LOGGER.info("You are on latest version!");
                return null;
            } catch (Throwable t) {
                LOGGER.error("Failed to check for updates!", t);
            }
            return null;
        };
    }
}
