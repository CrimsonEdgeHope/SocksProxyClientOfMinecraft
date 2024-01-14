package crimsonedgehope.minecraft.fabric.socksproxyclient;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocksProxyClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(SocksProxyClient.class);

	@Override
	public void onInitializeClient() {
		AutoConfig.register(SocksProxyClientConfig.class, GsonConfigSerializer::new);
		LOGGER.info("SocksProxyClient on.");
	}

}
