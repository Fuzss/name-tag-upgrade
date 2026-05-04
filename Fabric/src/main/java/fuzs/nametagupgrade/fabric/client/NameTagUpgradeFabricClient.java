package fuzs.nametagupgrade.fabric.client;

import fuzs.nametagupgrade.common.NameTagUpgrade;
import fuzs.nametagupgrade.common.client.NameTagUpgradeClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class NameTagUpgradeFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(NameTagUpgrade.MOD_ID, NameTagUpgradeClient::new);
    }
}
