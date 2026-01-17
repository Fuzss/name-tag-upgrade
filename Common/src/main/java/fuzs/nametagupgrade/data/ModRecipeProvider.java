package fuzs.nametagupgrade.data;

import fuzs.nametagupgrade.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        this.shaped(RecipeCategory.TOOLS, Items.NAME_TAG)
                .define('X', ModRegistry.NUGGETS_ITEM_TAG)
                .define('#', Items.PAPER)
                .pattern(" X")
                .pattern("# ")
                .unlockedBy(getHasName(ModRegistry.NUGGETS_ITEM_TAG), this.has(ModRegistry.NUGGETS_ITEM_TAG))
                .unlockedBy(getHasName(Items.PAPER), this.has(Items.PAPER))
                .unlockedBy(getHasName(Items.NAME_TAG), this.has(Items.NAME_TAG))
                .save(this.output);
    }
}
