package me.blazingtide.zetsu.adapters.defaults;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DoubleTypeAdapter implements ParameterAdapter<Double> {

    @Override
    public Double process(String str) {
        return Double.valueOf(str);
    }

    @Override
    public void processException(CommandSender sender, String given, Exception exception) {
        sender.sendMessage(ChatColor.RED + "'" + given + "' is not a valid number.");
    }
    
}
