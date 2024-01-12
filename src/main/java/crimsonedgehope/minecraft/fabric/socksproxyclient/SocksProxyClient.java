package crimsonedgehope.minecraft.fabric.socksproxyclient;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocksProxyClient implements ClientModInitializer {

	public static final String MOD_ID = "socksproxyclient";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("SocksProxyClient...");
		AutoConfig.register(SocksProxyClientConfig.class, GsonConfigSerializer::new);
	}

}
