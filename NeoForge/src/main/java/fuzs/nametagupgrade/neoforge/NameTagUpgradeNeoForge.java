package fuzs.nametagupgrade.neoforge;

import fuzs.nametagupgrade.common.NameTagUpgrade;
import fuzs.nametagupgrade.common.data.tags.ModEntityTagsProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(NameTagUpgrade.MOD_ID)
public class NameTagUpgradeNeoForge {

    public NameTagUpgradeNeoForge() {
        ModConstructor.construct(NameTagUpgrade.MOD_ID, NameTagUpgrade::new);
        DataProviderHelper.registerDataProviders(NameTagUpgrade.MOD_ID, ModEntityTagsProvider::new);
    }
}
