package fuzs.nametagupgrade.common.init;

import fuzs.nametagupgrade.common.NameTagUpgrade;
import fuzs.puzzleslib.common.api.init.v3.tags.TagFactory;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class ModRegistry {
    static final TagFactory TAGS = TagFactory.make(NameTagUpgrade.MOD_ID);
    public static final TagKey<EntityType<?>> NEVER_DROPS_NAME_TAG_ENTITY_TAG = TAGS.registerEntityTypeTag(
            "never_drops_name_tag");
    public static final TagKey<EntityType<?>> NEVER_SHEARS_CUSTOM_NAME_ENTITY_TAG = TAGS.registerEntityTypeTag(
            "never_shears_custom_name");
    public static final TagKey<EntityType<?>> NEVER_RETURNS_APPLIED_NAME_TAG_ENTITY_TAG = TAGS.registerEntityTypeTag(
            "never_returns_applied_name_tag");
    public static final TagKey<Item> NUGGETS_ITEM_TAG = TagFactory.COMMON.registerItemTag("nuggets");
    public static final TagKey<Item> SHEARS_ITEM_TAG = TagFactory.COMMON.registerItemTag("tools/shear");

    public static void bootstrap() {
        // NO-OP
    }
}
