package me.blazingtide.zetsu.adapters;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ParameterAdapter<T> {

    T process(String str);

    void processException(CommandSender sender, String given, Exception exception);

    default List<String> processTabComplete() {
        return null;
    }

}
