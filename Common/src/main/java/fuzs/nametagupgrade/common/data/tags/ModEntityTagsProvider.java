package fuzs.nametagupgrade.common.data.tags;

import fuzs.nametagupgrade.common.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;

public class ModEntityTagsProvider extends AbstractTagProvider<EntityType<?>> {

    public ModEntityTagsProvider(DataProviderContext context) {
        super(Registries.ENTITY_TYPE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(ModRegistry.NEVER_DROPS_NAME_TAG_ENTITY_TAG);
        this.tag(ModRegistry.NEVER_SHEARS_CUSTOM_NAME_ENTITY_TAG);
        this.tag(ModRegistry.OVERRIDES_CUSTOM_NAME_SHEARING_ENTITY_TAG)
                .add(EntityTypeIds.SHEEP, EntityTypeIds.SULFUR_CUBE);
        this.tag(ModRegistry.NEVER_RETURNS_APPLIED_NAME_TAG_ENTITY_TAG);
    }
}
