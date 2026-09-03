package fuzs.nametagupgrade.common.config;

import fuzs.puzzleslib.common.api.config.v3.Config;
import fuzs.puzzleslib.common.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    private static final String NAME_TAG_EDITING_CATEGORY = "name_tag_editing";
    private static final String REMOVE_USING_SHEARS_CATEGORY = "remove_using_shears";

    @Config(category = NAME_TAG_EDITING_CATEGORY,
            description = "Edit name tags without an anvil, simply from a handy screen opened via right-clicking.")
    public boolean editNameTagsWithoutAnvil = true;
    @Config(category = NAME_TAG_EDITING_CATEGORY,
            description = "The naming field in the name tag screen will support formatting codes for setting custom text colors and styles.")
    public boolean renamingSupportsFormatting = true;
    @Config(description = "Mobs that have a custom name drop a name tag with that name on death.")
    public boolean dropNameTagsOnDeath = true;
    @Config(category = REMOVE_USING_SHEARS_CATEGORY,
            description = "Mobs that have a custom name can have it removed using shears and drop a name tag with that name.")
    public boolean removeCustomNameUsingShears = true;
    @Config(category = REMOVE_USING_SHEARS_CATEGORY,
            description = "When removing a custom name using shears; prioritize shearing-off all leashes connected to the mob.")
    public boolean prioritizeShearingLeash = true;
    @Config(category = REMOVE_USING_SHEARS_CATEGORY,
            description = "When removing a custom name using shears; prioritize any mob-specific shearing behavior, such as shearing sheep.")
    public boolean prioritizeShearingBehavior = true;
    @Config(category = REMOVE_USING_SHEARS_CATEGORY,
            description = "When removing a custom name using shears; prioritize shearing-off equipment worn by the mob such as saddles or armor.")
    public boolean prioritizeShearingEquipment = false;
    @Config(description = "When renaming an already named mob, the old name tag will drop as an item when the new name tag is applied.")
    public boolean returnAppliedNameTags = true;
    @Config(description = "When trying to use a name tag on a mob already named with that exact name nothing will be consumed.")
    public boolean preventVoidingNameTags = true;
    @Config(description = "Play a plopping sound when using a name tag on a mob.")
    public boolean playNameTagSound = true;
}
