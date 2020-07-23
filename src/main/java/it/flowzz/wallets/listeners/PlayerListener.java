package it.flowzz.wallets.listeners;

import it.flowzz.wallets.WalletPlugin;
import it.flowzz.wallets.utils.InvUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;

public class PlayerListener implements Listener {

    private WalletPlugin plugin;

    public PlayerListener(WalletPlugin plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onWalletInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getHand() != EquipmentSlot.HAND) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;

        Material walletMat = Material.valueOf(plugin.getConfig().getString("wallet-item.material"));
        int modelData = plugin.getConfig().getInt("wallet-item.custommodeldata");

        if(player.getItemInHand().getType() == walletMat &&
                player.getItemInHand().hasItemMeta() &&
                player.getItemInHand().getItemMeta().getCustomModelData() == modelData){
            try { player.openInventory(getWallet(player.getItemInHand())); } catch (IOException e) { e.printStackTrace(); }
        }
    }


    private Inventory getWallet(ItemStack wallet) throws IOException {
        NamespacedKey key = new NamespacedKey(plugin, "wallet-contents");
        String data = wallet.getItemMeta().getPersistentDataContainer().get(key,PersistentDataType.STRING);
        String invTitle = ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("wallet.title"));

        if(data == null){
            return Bukkit.createInventory(null,plugin.getConfig().getInt("wallet.size"), invTitle);
        }
        return InvUtils.inventoryFromBase64(data, invTitle);

    }
}
