/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.sanecrafting;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.sanecrafting.patches.EnhancedCraftingTablePatch;
import org.metamechanists.sanecrafting.patches.UsableInWorkbenchPatch;


public final class SaneCrafting extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static SaneCrafting instance;

    @Override
    public void onEnable() {
        instance = this;

        // Patches applied on first tick to ensure everything has loaded
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            UsableInWorkbenchPatch.apply();
            EnhancedCraftingTablePatch.apply();
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
