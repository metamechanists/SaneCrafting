package org.metamechanists.sanecrafting.patches;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


@UtilityClass
public class ChangeRecipeTypePatch {
    private final ItemStack craftingTableRecipe = new CustomItemStack(Material.CRAFTING_TABLE, "Crafting Table");

    private void apply() {
        try {
            Field field = RecipeType.ENHANCED_CRAFTING_TABLE.getClass().getField("");
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(null, craftingTableRecipe);
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
            Bukkit.getLogger().info("Failed to apply ChangeRecipeTypePatch");
            e.printStackTrace();
        }
    }
}
