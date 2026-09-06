package fuzs.nametagupgrade.common.client;

import fuzs.nametagupgrade.common.NameTagUpgrade;
import fuzs.nametagupgrade.common.client.gui.screens.inventory.NameTagEditScreen;
import fuzs.nametagupgrade.common.config.ClientConfig;
import fuzs.nametagupgrade.common.config.ServerConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class NameTagUpgradeClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        PlayerInteractEvents.USE_ITEM.register(NameTagUpgradeClient::onUseItem);
    }

    public static EventResultHolder<InteractionResult> onUseItem(Player player, Level level, InteractionHand interactionHand) {
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).editNameTagsWithoutAnvil) {
            return EventResultHolder.pass();
        }

        if (level.isClientSide()) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (itemInHand.is(Items.NAME_TAG)
                    && NameTagUpgrade.CONFIG.get(ClientConfig.class).openNameTagEditScreen.canOpen(player,
                    itemInHand)) {
                Minecraft.getInstance().gui.setScreen(new NameTagEditScreen(itemInHand, interactionHand));
                return EventResultHolder.interrupt(InteractionResult.SUCCESS);
            }
        }

        return EventResultHolder.pass();
    }
}
