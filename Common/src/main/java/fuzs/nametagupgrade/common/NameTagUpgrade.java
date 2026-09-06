package fuzs.nametagupgrade.common;

import fuzs.nametagupgrade.common.config.ClientConfig;
import fuzs.nametagupgrade.common.config.ServerConfig;
import fuzs.nametagupgrade.common.handler.NameTagDropHandler;
import fuzs.nametagupgrade.common.init.ModRegistry;
import fuzs.nametagupgrade.common.network.client.ServerboundEditNameTagMessage;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDropsCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NameTagUpgrade implements ModConstructor {
    public static final String MOD_ID = "nametagupgrade";
    public static final String MOD_NAME = "Name Tag Upgrade";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID)
            .client(ClientConfig.class)
            .server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        LivingDropsCallback.EVENT.register(NameTagDropHandler::onLivingDrops);
        PlayerInteractEvents.USE_ENTITY.register(NameTagDropHandler::onUseEntity);
    }

    @Override
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.playToServer(ServerboundEditNameTagMessage.class, ServerboundEditNameTagMessage.STREAM_CODEC);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
