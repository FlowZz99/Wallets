package it.flowzz.wallets.listeners;

import it.flowzz.wallets.WalletPlugin;
import it.flowzz.wallets.utils.InvUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InventoryListener implements Listener {


    private WalletPlugin plugin;

    public InventoryListener(WalletPlugin plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onWalletClose(InventoryCloseEvent event){
        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("wallet.title"))))
            saveWallet(event.getPlayer().getItemInHand(),event.getInventory());
    }

    @EventHandler
    public void onWalletClick(InventoryClickEvent event){
        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("wallet.title")))){
            if(event.getCurrentItem() != null && !isWhitelist(event.getCurrentItem()) ){
                event.setResult(Event.Result.DENY);
                event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Lang.item-not-whitelisted")));
                return;
            }
            if (event.getHotbarButton() >= 0 && !isWhitelist(event.getWhoClicked().getInventory().getItem(event.getHotbarButton()))) {
                event.setResult(Event.Result.DENY);
                event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Lang.item-not-whitelisted")));
            }
        }
    }

    private boolean isWhitelist(ItemStack itemStack) {
        return plugin.getAllowedItems().stream().anyMatch(stack -> stack.getType().equals(itemStack.getType()) &&
                stack.getItemMeta().getCustomModelData() == itemStack.getItemMeta().getCustomModelData());
    }

    private void saveWallet(ItemStack wallet, Inventory inventory) {
        NamespacedKey key = new NamespacedKey(plugin, "wallet-contents");
        ItemMeta walletMeta = wallet.getItemMeta();
        walletMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, InvUtils.inventoryToBase64(inventory));
        wallet.setItemMeta(walletMeta);
    }

}
