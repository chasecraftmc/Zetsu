package me.blazingtide.zetsu.processor.impl;

import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.processor.CommandProcessor;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.blazingtide.zetsu.schema.annotations.parameter.Default;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;

public class SpigotProcessor extends CommandProcessor implements CommandExecutor {

    public SpigotProcessor(Zetsu zetsu) {
        super(zetsu);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final CachedCommand found = this.find(label, args);

        if (found == null) {
            sender.sendMessage(ChatColor.RED + "Library Error. (Command not found)");
            return false;
        }

        int defaults = 0;

        for (Parameter parameter : found.getMethod().getParameters()) {
            defaults += parameter.isAnnotationPresent(Default.class) ? 1 : 0;
        }

        for (int i = 0; i < found.getArgs().size() + defaults; i++) {
            if (args.length > i) {
                args = (String[]) ArrayUtils.remove(args, i);
            }
        }

        this.invoke(found, args, sender);
        return false;
    }
}
