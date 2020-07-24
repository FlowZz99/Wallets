package it.flowzz.wallets.commands;

import it.flowzz.wallets.WalletPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WalletCMD implements CommandExecutor {

    private WalletPlugin plugin;

    public WalletCMD(WalletPlugin plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("wallet.give")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.nopermission")));
            return false;
        }
        if (args.length != 2) {
            sendSyntax(sender);
            return false;
        }
        if (args[0].equalsIgnoreCase("give")) {
            Player p = Bukkit.getPlayer(args[1]);
            if (p == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.playeroffline")));
                return false;
            }
            p.getInventory().addItem(getWallet());
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.wallet-received")));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.wallet-give")));
            return true;
        }
        sendSyntax(sender);
        return false;
    }

    private ItemStack getWallet() {
        Material walletMat = Material.valueOf(plugin.getConfig().getString("wallet-item.material"));
        int modelData = plugin.getConfig().getInt("wallet-item.custommodeldata");

        ItemStack wallet = new ItemStack(walletMat,1);
        ItemMeta walletMeta = wallet.getItemMeta();
        walletMeta.setCustomModelData(modelData);
        wallet.setItemMeta(walletMeta);
        return wallet;
    }

    private void sendSyntax(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.command-syntax")));
    }

    private int getIntFromString(String s) {
        int x;
        try { x = Integer.parseInt(s); }
        catch (NumberFormatException e) { x = 0; }
        return x;
    }
}
