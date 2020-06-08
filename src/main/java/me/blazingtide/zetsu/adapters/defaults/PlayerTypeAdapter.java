package me.blazingtide.zetsu.adapters.defaults;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerTypeAdapter implements ParameterAdapter<Player> {

    @Override
    public Player process(String str) {
        return Bukkit.getPlayer(str);
    }

    @Override
    public void processException(CommandSender sender, String given, Exception exception) {
        sender.sendMessage(ChatColor.RED + "'" + given + "' is not online.");
    }
    
}
