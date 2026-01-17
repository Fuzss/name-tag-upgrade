package fuzs.nametagupgrade.neoforge;

import fuzs.nametagupgrade.NameTagUpgrade;
import fuzs.nametagupgrade.data.ModRecipeProvider;
import fuzs.nametagupgrade.data.tags.ModEntityTagsProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.minecraft.server.packs.PackType;
import net.neoforged.fml.common.Mod;

@Mod(NameTagUpgrade.MOD_ID)
public class NameTagUpgradeNeoForge {

    public NameTagUpgradeNeoForge() {
        ModConstructor.construct(NameTagUpgrade.MOD_ID, NameTagUpgrade::new);
        DataProviderHelper.registerDataProviders(NameTagUpgrade.MOD_ID, ModEntityTagsProvider::new);
        DataProviderHelper.registerDataProviders(NameTagUpgrade.NAME_TAG_RECIPE_ID,
                PackType.SERVER_DATA,
                ModRecipeProvider::new);
    }
}
