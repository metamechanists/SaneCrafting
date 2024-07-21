package org.metamechanists.sanecrafting.patches;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


// Once, I saw the mirror in my dreams. It spoke to me.
// 'Idra, go forth, and take my insights. It is time to apply the Holy principles of Reflection for the betterment of Humanity.'
// I woke up, and wrote this code. My life has been 10x better ever since. Follow the mirror.
@UtilityClass
public class RecipeLorePatch {
    private final ItemStack craftingTableRecipe = new CustomItemStack(Material.CRAFTING_TABLE, "Crafting Table");

    public void apply() {
        try {
            Field field = RecipeType.ENHANCED_CRAFTING_TABLE.getClass().getField("item");
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
