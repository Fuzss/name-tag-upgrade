package fuzs.nametagupgrade.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(description = "Edit name tags without an anvil, simply from a handy screen opened via right-clicking.")
    public boolean editNameTagsWithoutAnvil = true;
    @Config(description = "The naming field in the name tag screen will support formatting codes for setting custom text colors and styles.")
    public boolean renamingSupportsFormatting = true;
    @Config(description = "Mobs that have a custom name drop a name tag with that name on death.")
    public boolean dropNameTagsOnDeath = true;
    @Config(description = "Mobs that have a custom name can have it removed using shears and drop a name tag with that name.")
    public boolean removeCustomNameUsingShears = true;
    @Config(description = "When renaming an already named mob, the old name tag will drop as an item when the new name tag is applied.")
    public boolean returnAppliedNameTags = true;
    @Config(description = "When trying to use a name tag on a mob already named with that exact name nothing will be consumed.")
    public boolean preventVoidingNameTags = true;
    @Config(description = "Play a plopping sound when using a name tag on a mob.")
    public boolean playNameTagSound = true;
}
