package fuzs.nametagupgrade.common.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    @Config(description = "Control how the name tag edit screen opens when holding the item and right-clicking.")
    public OpenNameTagEditScreen openNameTagEditScreen = OpenNameTagEditScreen.SNEAKING_WHEN_RENAMED;
}
