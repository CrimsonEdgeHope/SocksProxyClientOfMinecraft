package crimsonedgehope.minecraft.fabric.socksproxyclient;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocksProxyClient implements ClientModInitializer {

	private static final String LOGGER_NAME = "SocksProxyClient";
	private static final Logger LOGGER = LoggerFactory.getLogger(LOGGER_NAME);

	@Override
	public void onInitializeClient() {
		initConfig();
		logger().info("SocksProxyClient on.");
		logger().debug("SocksProxyClient debug logging on.");
	}

	private static void initConfig() {
		AutoConfig.register(SocksProxyClientConfig.class, GsonConfigSerializer::new);
	}

	public static Logger logger() {
		return LOGGER;
	}

}
