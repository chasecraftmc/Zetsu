package me.blazingtide.zetsu.adapters.defaults;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BooleanTypeAdapter implements ParameterAdapter<Boolean> {

    @Override
    public Boolean process(String str) {
        if (str.equalsIgnoreCase("yes")) {
            return true;
        } else if (str.equalsIgnoreCase("no")) {
            return false;
        }
        return Boolean.valueOf(str);
    }

    @Override
    public void processException(CommandSender sender, String given, Exception exception) {
        sender.sendMessage(ChatColor.RED + "'" + given + "' is not a valid boolean.");
    }
    
}
