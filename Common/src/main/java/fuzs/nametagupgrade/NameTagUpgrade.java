package fuzs.nametagupgrade;

import fuzs.nametagupgrade.config.ClientConfig;
import fuzs.nametagupgrade.config.ServerConfig;
import fuzs.nametagupgrade.handler.NameTagDropHandler;
import fuzs.nametagupgrade.init.ModRegistry;
import fuzs.nametagupgrade.network.client.ServerboundEditNameTagMessage;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDropsCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NameTagUpgrade implements ModConstructor {
    public static final String MOD_ID = "nametagupgrade";
    public static final String MOD_NAME = "Name Tag Upgrade";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID)
            .client(ClientConfig.class)
            .server(ServerConfig.class);
    public static final Identifier NAME_TAG_RECIPE_ID = id("name_tag_recipe");

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

    @Override
    public void onAddDataPackFinders(PackRepositorySourcesContext context) {
        context.registerBuiltInPack(NAME_TAG_RECIPE_ID, Component.literal("Name Tag Recipe"), true);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
