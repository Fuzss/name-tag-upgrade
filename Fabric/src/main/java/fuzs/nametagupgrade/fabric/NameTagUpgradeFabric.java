package fuzs.nametagupgrade.fabric;

import fuzs.nametagupgrade.common.NameTagUpgrade;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class NameTagUpgradeFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(NameTagUpgrade.MOD_ID, NameTagUpgrade::new);
    }
}
