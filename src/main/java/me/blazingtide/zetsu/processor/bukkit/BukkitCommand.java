package me.blazingtide.zetsu.processor.bukkit;

import me.blazingtide.zetsu.processor.impl.SpigotProcessor;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BukkitCommand extends Command {

    private final SpigotProcessor processor;
    private final TabCompleteHandler handler;

    public BukkitCommand(String name, SpigotProcessor processor, TabCompleteHandler handler) {
        super(name);
        this.processor = processor;
        this.handler = handler;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return processor.onCommand(commandSender, this, s, strings);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return handler.getListener().onTabComplete(sender, this, alias, args); //It's easier to separate chunks of code into different classes.
    }
}
