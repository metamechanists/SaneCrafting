/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.sanecrafting;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public final class SaneCrafting extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static SaneCrafting instance;

    @Override
    public void onEnable() {
        instance = this;

        // lol
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
                if (!item.getRecipeType().equals(RecipeType.ENHANCED_CRAFTING_TABLE)) {
                    continue;
                }

                ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, item.getId()), item.getItem());
                getServer().getLogger().info("" + item.getRecipe().length);
                List<ItemStack> items = Arrays.asList(item.getRecipe());

                // Convert to shape
                String itemCharacters = "abcdefghi";
                List<String> shape = new ArrayList<>(List.of("abc", "def", "ghi"));
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        int i = x*3 + y;
                        char character = itemCharacters.charAt(x);
                        if (items.get(i) == null) {
                            shape.set(y, shape.get(y).replace(character, ' '));
                        } else {
                            recipe.setIngredient(character, item.getItem());
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

                recipe.shape(shape.toArray(new String[]{}));

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
