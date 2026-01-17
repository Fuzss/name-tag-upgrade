package fuzs.nametagupgrade.neoforge.client;

import fuzs.nametagupgrade.NameTagUpgrade;
import fuzs.nametagupgrade.client.NameTagUpgradeClient;
import fuzs.nametagupgrade.data.client.ModLanguageProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = NameTagUpgrade.MOD_ID, dist = Dist.CLIENT)
public class NameTagUpgradeNeoForgeClient {

    public NameTagUpgradeNeoForgeClient() {
        ClientModConstructor.construct(NameTagUpgrade.MOD_ID, NameTagUpgradeClient::new);
        DataProviderHelper.registerDataProviders(NameTagUpgrade.MOD_ID, ModLanguageProvider::new);
    }
}
