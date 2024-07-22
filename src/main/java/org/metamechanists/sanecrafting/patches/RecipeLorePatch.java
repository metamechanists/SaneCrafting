package org.metamechanists.sanecrafting.patches;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;


// Once, I saw the mirror in my dreams. It spoke to me.
// 'Idra, go forth, and take my insights. It is time to apply the Holy principles of Reflection for the betterment of Humanity.'
// I woke up, and wrote this code. My life has been 10x better ever since. Follow the mirror.
@UtilityClass
public class RecipeLorePatch {
    // god almighty what am I doing
    private final RecipeType FAKE_ENHANCED_CRAFTING_TABLE = new FakeCraftingTableType(
            new NamespacedKey(Slimefun.instance(), "fake_enhanced_crafting_table"),
            SlimefunItems.ENHANCED_CRAFTING_TABLE,
            "", "&a&oA regular Crafting Table cannot", "&a&ohold this massive Amount of Power...");

    class FakeCraftingTableType extends RecipeType {
        FakeCraftingTableType(NamespacedKey key, SlimefunItemStack slimefunItem, String... lore) {
            super(key, slimefunItem, lore);
        }

        // lmao
        @Override
        public @NotNull ItemStack getItem(Player p) {
            return Slimefun.getLocalization().getRecipeTypeItem(p, RecipeType.SMELTERY);
        }
    }

    public void apply() {
        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            if (!item.getRecipeType().equals(RecipeType.ENHANCED_CRAFTING_TABLE)) {
                continue;
            }

            try {
                Field field = item.getClass().getField("recipeType");
                field.setAccessible(true);

                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

                field.set(null, FAKE_ENHANCED_CRAFTING_TABLE);
            } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
                Bukkit.getLogger().info("Failed to apply ChangeRecipeTypePatch");
                e.printStackTrace();
            }
        }
    }
}
