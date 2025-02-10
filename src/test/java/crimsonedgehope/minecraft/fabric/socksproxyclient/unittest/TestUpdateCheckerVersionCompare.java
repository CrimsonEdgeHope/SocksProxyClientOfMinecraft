package crimsonedgehope.minecraft.fabric.socksproxyclient.unittest;

import com.terraformersmc.modmenu.api.UpdateChannel;
import com.terraformersmc.modmenu.api.UpdateInfo;
import crimsonedgehope.minecraft.fabric.socksproxyclient.modmenu.SocksProxyClientUpdateChecker;
import net.fabricmc.loader.api.Version;
import net.minecraft.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestUpdateCheckerVersionCompare {
    
    @BeforeAll
    static void prepare() {
        Utils.preBootstrap();
    }
    
    private void compareAssertAvailable(Pair<String, String>[] items, String releaseChannel, UpdateChannel updateChannel) throws Exception {
        UpdateInfo info;
        
        for (Pair<String, String> item : items) {
            info = SocksProxyClientUpdateChecker.compare(Version.parse(item.getLeft()), Version.parse(item.getRight()), releaseChannel);
            Assertions.assertNotNull(info);
            Assertions.assertTrue(info.isUpdateAvailable());
            Assertions.assertEquals(info.getDownloadLink(), SocksProxyClientUpdateChecker.MODRINTH);
            Assertions.assertEquals(info.getUpdateChannel(), updateChannel);
        }
    }
    
    private void compareAssertNone(Pair<String, String>[] items, String releaseChannel) throws Exception {
        UpdateInfo info;

        for (Pair<String, String> item : items) {
            info = SocksProxyClientUpdateChecker.compare(Version.parse(item.getLeft()), Version.parse(item.getRight()), releaseChannel);
            Assertions.assertNull(info);
        }
    }
    
    @Test
    @DisplayName("Test version comparison for update checker")
    void testCompare() {
        Assertions.assertDoesNotThrow(() -> {
            final Pair<String, String>[] items = new Pair[]{
                    new Pair<>("1.0.1-alpha", "1.0.0-alpha"),
                    new Pair<>("1.0.2-alpha", "1.0.0-alpha"),
            };
            compareAssertAvailable(items, "alpha", UpdateChannel.ALPHA);
        });
        
        Assertions.assertDoesNotThrow(() -> {
            final Pair<String, String>[] items = new Pair[]{
                    new Pair<>("1.0.1-alpha", "1.0.1-alpha"),
                    new Pair<>("1.0.0-alpha", "1.0.1-alpha"),
                    new Pair<>("0.9.9-alpha", "1.0.1-alpha"),
            };
            compareAssertNone(items, "alpha");
        });
    }
}
