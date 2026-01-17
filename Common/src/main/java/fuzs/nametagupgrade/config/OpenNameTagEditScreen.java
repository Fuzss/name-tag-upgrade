package fuzs.nametagupgrade.config;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public enum OpenNameTagEditScreen {
    ALWAYS {
        @Override
        public boolean canOpen(Player player, ItemStack itemStack) {
            return true;
        }
    },
    SNEAKING {
        @Override
        public boolean canOpen(Player player, ItemStack itemStack) {
            return player.isSecondaryUseActive();
        }
    },
    SNEAKING_WHEN_RENAMED {
        @Override
        public boolean canOpen(Player player, ItemStack itemStack) {
            return player.isSecondaryUseActive() || !itemStack.has(DataComponents.CUSTOM_NAME);
        }
    },
    NEVER_WHEN_RENAMED {
        @Override
        public boolean canOpen(Player player, ItemStack itemStack) {
            return !itemStack.has(DataComponents.CUSTOM_NAME);
        }
    };

    public abstract boolean canOpen(Player player, ItemStack itemStack);
}
