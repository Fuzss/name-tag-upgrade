package fuzs.nametagupgrade.common.network.client;

import fuzs.nametagupgrade.common.util.FormattedStringUtil;
import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record ServerboundEditNameTagMessage(InteractionHand interactionHand,
                                            String itemName) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundEditNameTagMessage> STREAM_CODEC = StreamCodec.composite(
            ExtraStreamCodecs.fromEnum(InteractionHand.class),
            ServerboundEditNameTagMessage::interactionHand,
            ByteBufCodecs.STRING_UTF8,
            ServerboundEditNameTagMessage::itemName,
            ServerboundEditNameTagMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                ItemStack itemInHand = context.player()
                        .getItemInHand(ServerboundEditNameTagMessage.this.interactionHand);
                if (itemInHand.is(Items.NAME_TAG)) {
                    String itemName = FormattedStringUtil.filterText(ServerboundEditNameTagMessage.this.itemName);
                    if (FormattedStringUtil.stringLength(itemName) <= 50) {
                        setFormattedItemName(itemInHand, itemName.trim());
                    }
                }
            }

            public static void setFormattedItemName(ItemStack itemStack, String itemName) {
                Component component = FormattedStringUtil.getAsComponent(itemName);
                if (component.getString().isEmpty()) {
                    itemStack.remove(DataComponents.CUSTOM_NAME);
                } else {
                    itemStack.set(DataComponents.CUSTOM_NAME, component);
                }
            }
        };
    }
}
