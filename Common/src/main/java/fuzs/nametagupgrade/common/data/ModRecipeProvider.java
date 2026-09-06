package fuzs.nametagupgrade.common.data;

import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        TagKey<Item> nuggetsTag = TagFactory.COMMON.registerItemTag("nuggets");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.NAME_TAG)
                .define('X', nuggetsTag)
                .define('#', Items.PAPER)
                .pattern(" X")
                .pattern("# ")
                .unlockedBy(getHasName(nuggetsTag), has(nuggetsTag))
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .unlockedBy(getHasName(Items.NAME_TAG), has(Items.NAME_TAG))
                .save(recipeOutput);
    }
}
