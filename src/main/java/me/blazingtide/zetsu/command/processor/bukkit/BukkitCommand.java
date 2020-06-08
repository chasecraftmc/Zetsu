package me.blazingtide.zetsu.command.processor.bukkit;

import me.blazingtide.zetsu.command.processor.impl.SpigotProcessor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BukkitCommand extends Command {

    private final SpigotProcessor processor;

    public BukkitCommand(String name, SpigotProcessor processor) {
        super(name);
        this.processor = processor;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return processor.onCommand(commandSender, this, s, strings);
    }
}
