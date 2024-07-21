package org.metamechanists.sanecrafting.patches;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.Smeltery;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.sanecrafting.SaneCrafting;
import org.metamechanists.sanecrafting.Util;

import java.util.List;

import static org.metamechanists.sanecrafting.Util.generateRecipeId;


@UtilityClass
public class FurnacePatch {
    private @Nullable List<ItemStack[]> getRecipes() {
        Smeltery smeltery = Util.findMultiblock(Smeltery.class);
        if (smeltery == null) {
            return null;
        }
        return smeltery.getRecipes();
    }

    private void convertRecipe(ItemStack input, ItemStack output, int recipeIndex) {
        String id = generateRecipeId(output, recipeIndex);
        NamespacedKey key = new NamespacedKey(SaneCrafting.getInstance(), id);
        FurnaceRecipe recipe = new FurnaceRecipe(key, output, new ExactChoice(input), 0.0F, 10); // TODO config for time
        Bukkit.getServer().addRecipe(recipe);
    }

    public void apply() {
        List<ItemStack[]> recipes = getRecipes();
        if (recipes == null) {
            return;
        }

        int changedRecipes = 0;
        for (int j = 0; j < recipes.size(); j += 2) {
            ItemStack[] inputArray = recipes.get(j);
            ItemStack output = recipes.get(j + 1)[0];

            if (inputArray.length != 1) {
                continue;
            }

            ItemStack input = inputArray[0];

            try {
                convertRecipe(input, output, j / 2);
            } catch (RuntimeException e) {
                String name = PlainTextComponentSerializer.plainText().serialize(output.displayName());
                Bukkit.getLogger().severe("Failed to convert single-item Smeltery recipe for " + name);
                e.printStackTrace();
                continue;
            }

            changedRecipes++;
        }

        Bukkit.getLogger().info("Converted " + changedRecipes + " single-item Smeltery recipes to regular Furnace recipes");
    }
}
