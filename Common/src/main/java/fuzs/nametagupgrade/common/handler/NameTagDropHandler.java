package fuzs.nametagupgrade.common.handler;

import fuzs.nametagupgrade.common.NameTagUpgrade;
import fuzs.nametagupgrade.common.config.ServerConfig;
import fuzs.nametagupgrade.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.event.v1.core.EventResult;
import fuzs.puzzleslib.common.api.event.v1.core.EventResultHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

public class NameTagDropHandler {

    public static EventResult onLivingDrops(LivingEntity livingEntity, DamageSource damageSource, Collection<ItemEntity> itemDrops, boolean recentlyHit) {
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).dropNameTagsOnDeath) {
            return EventResult.PASS;
        }

        if (livingEntity.getType().canSerialize() && !livingEntity.is(ModRegistry.NEVER_DROPS_NAME_TAG_ENTITY_TAG)
                && livingEntity.hasCustomName()) {
            ItemStack itemStack = new ItemStack(Items.NAME_TAG);
            itemStack.set(DataComponents.CUSTOM_NAME, livingEntity.getCustomName());
            ItemEntity itemEntity = new ItemEntity(livingEntity.level(),
                    livingEntity.getX(),
                    livingEntity.getEyeY(),
                    livingEntity.getZ(),
                    itemStack);
            itemEntity.setDefaultPickUpDelay();
            itemDrops.add(itemEntity);
        }

        return EventResult.PASS;
    }

    public static EventResultHolder<InteractionResult> onUseEntity(Player player, Level level, InteractionHand interactionHand, Entity entity, Vec3 hitVector) {
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        if (NameTagUpgrade.CONFIG.get(ServerConfig.class).removeCustomNameUsingShears) {
            if (itemInHand.is(ModRegistry.SHEARS_ITEM_TAG)
                    && !entity.is(ModRegistry.NEVER_SHEARS_CUSTOM_NAME_ENTITY_TAG)) {
                if (!(entity instanceof Leashable leashable) || !leashable.isLeashed()) {
                    InteractionResult interactionResult = shearOffCustomName(player, level, entity);
                    if (interactionResult.consumesAction()) {
                        itemInHand.hurtAndBreak(1, player, interactionHand);
                        return EventResultHolder.interrupt(interactionResult);
                    }
                }
            }
        }

        if (itemInHand.is(Items.NAME_TAG) && itemInHand.has(DataComponents.CUSTOM_NAME)) {
            if (entity.getType().canSerialize() && entity.isAlive()) {
                if (NameTagUpgrade.CONFIG.get(ServerConfig.class).preventVoidingNameTags) {
                    if (Objects.equals(entity.getCustomName(), itemInHand.get(DataComponents.CUSTOM_NAME))) {
                        return EventResultHolder.interrupt(InteractionResult.PASS);
                    }
                }

                if (NameTagUpgrade.CONFIG.get(ServerConfig.class).playNameTagSound) {
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null,
                                entity.blockPosition(),
                                SoundEvents.ITEM_PICKUP,
                                player.getSoundSource(),
                                0.2F,
                                0.8F + serverLevel.getRandom().nextFloat() * 0.4F);
                    }
                }

                if (NameTagUpgrade.CONFIG.get(ServerConfig.class).returnAppliedNameTags) {
                    if (!entity.is(ModRegistry.NEVER_RETURNS_APPLIED_NAME_TAG_ENTITY_TAG) && entity.hasCustomName()) {
                        if (level instanceof ServerLevel serverLevel) {
                            spawnNameTagItem(serverLevel, entity);
                        }
                    }
                }
            }
        }

        return EventResultHolder.pass();
    }

    /**
     * @see Entity#shearOffAllLeashConnections(Player)
     */
    private static InteractionResult shearOffCustomName(@Nullable Player player, Level level, Entity entity) {
        if (entity.hasCustomName()) {
            if (level instanceof ServerLevel serverLevel) {
                spawnNameTagItem(serverLevel, entity);
                entity.gameEvent(GameEvent.SHEAR, player);
                serverLevel.playSound(null,
                        entity.blockPosition(),
                        SoundEvents.SHEARS_SNIP,
                        player != null ? player.getSoundSource() : entity.getSoundSource());
            }

            entity.setCustomName(null);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    private static void spawnNameTagItem(ServerLevel serverLevel, Entity entity) {
        ItemStack itemStack = new ItemStack(Items.NAME_TAG);
        itemStack.set(DataComponents.CUSTOM_NAME, entity.getCustomName());
        entity.spawnAtLocation(serverLevel, itemStack, entity.getEyeHeight());
    }
}
