/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.sanecrafting;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;


public final class SaneCrafting extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static SaneCrafting instance;

    @Override
    public void onEnable() {
        instance = this;

        // lol
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            EnhancedCraftingTable enhancedCraftingTable = null;
            for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
                if (item instanceof EnhancedCraftingTable table) {
                    enhancedCraftingTable = table;
                }
            }
            if (enhancedCraftingTable == null) {
                getServer().getLogger().severe("Failed to initialise SaneCrafting; EnhancedCraftingTable does not exist!");
                return;
            }

            List<ItemStack[]> recipes = enhancedCraftingTable.getRecipes();
            for (int j = 0; j < recipes.size(); j += 2) {
                ItemStack[] input = recipes.get(j);
                ItemStack output = recipes.get(j+1)[0];

                List<ItemStack> items = Arrays.asList(input);

                // Convert to shape
                String itemCharacters = "abcdefghi";
                List<String> shape = new ArrayList<>(List.of("abc", "def", "ghi"));
                Map<Character, ItemStack> ingredients = new HashMap<>();
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        int i = x*3 + y;
                        char character = itemCharacters.charAt(i);
                        ItemStack itemStack = items.get(i);
                        if (itemStack == null) {
                            shape.set(y, shape.get(y).replace(character, ' '));
                        } else {
                            ingredients.put(character, itemStack);
                        }
                    }
                }

                // Trim vertical
                for (int y = shape.size() - 1; y > 0; y--) {
                    if (Objects.equals(shape.get(y), "   ")) {
                        shape.remove(y);
                    }
                }

                // Skip if no recipe (just in case)
                if (shape.isEmpty()) {
                    continue;
                }

                // Trim horizontal
                for (int x = shape.get(0).length() - 1; x > 0; x--) {
                    boolean allRowsEmptyAtX = true;
                    for (int y = shape.size() - 1; y > 0; y--) {
                        if (shape.get(y).charAt(x) != ' ') {
                            allRowsEmptyAtX = false;
                            break;
                        }
                    }
                    if (allRowsEmptyAtX) {
                        for (int y = shape.size() - 1; y > 0; y--) {
                            getServer().getLogger().info("yeeting " + shape.get(y));
                            String newRow = new StringBuilder(shape.get(y))
                                    .deleteCharAt(x)
                                    .toString();
                            shape.set(y, newRow);
                        }
                    }
                }

                getServer().getLogger().info(Arrays.toString(shape.toArray()));

                // Use index to avoid ID clash if two recipes for same item
                String id = "sanecrafting_" + j / 2 + "_"
                        + PlainTextComponentSerializer.plainText()
                            .serialize(output.displayName())
                            .replace(' ', '_')
                            .toLowerCase();

                getServer().getLogger().info(id);

                ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, id), output);
                recipe.shape(shape.toArray(new String[]{}));
                for (Entry<Character, ItemStack> entry : ingredients.entrySet()) {
                    recipe.setIngredient(entry.getKey(), entry.getValue());
                }
                getServer().addRecipe(recipe);
            }
        }, 1);
    }

    @Override
    public void onDisable() {

    }

    @NonNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return null;
    }
}
