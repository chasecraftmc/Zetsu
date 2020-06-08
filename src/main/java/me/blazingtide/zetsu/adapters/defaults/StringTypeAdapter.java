package me.blazingtide.zetsu.adapters.defaults;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import org.bukkit.command.CommandSender;

public class StringTypeAdapter implements ParameterAdapter<String> {

    @Override
    public String process(String str) {
        return str;
    }

    @Override
    public void processException(CommandSender sender, String given, Exception exception) {
        //Never
    }
}
