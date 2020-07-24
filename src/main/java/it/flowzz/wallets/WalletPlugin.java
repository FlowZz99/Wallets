package it.flowzz.wallets;

import it.flowzz.wallets.commands.WalletCMD;
import it.flowzz.wallets.listeners.InventoryListener;
import it.flowzz.wallets.listeners.PlayerListener;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class WalletPlugin extends JavaPlugin {

    @Getter
    private List<ItemStack> allowedItems;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        allowedItems = new ArrayList<>();
        new InventoryListener(this);
        new PlayerListener(this);
        getCommand("wallet").setExecutor(new WalletCMD(this));
        loadWhitelist();
    }

    private void loadWhitelist() {
        MemorySection node = (MemorySection) getConfig().get("whitelisted-items");
        node.getKeys(false).forEach(string -> {
            ItemStack whiteItem = new ItemStack(Material.valueOf(node.getString(string + ".material")));
            ItemMeta whiteMeta = whiteItem.getItemMeta();
            whiteMeta.setCustomModelData(node.getInt(string + ".custommodeldata"));
            whiteItem.setItemMeta(whiteMeta);
            allowedItems.add(whiteItem);
        });
    }
}
