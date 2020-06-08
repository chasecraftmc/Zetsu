package me.blazingtide.zetsu.processor.impl;

import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.blazingtide.zetsu.processor.CommandProcessor;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpigotProcessor extends CommandProcessor implements CommandExecutor {

    public SpigotProcessor(Zetsu zetsu) {
        super(zetsu);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final CachedCommand found = this.find(label, args, sender);

        if (found == null) {
            sender.sendMessage(ChatColor.RED + "Library Error. (Command not found)");
            return false;
        }

        //Just remove all of the subcommand stuff
        for (int i = 0; i < found.getArgs().size(); i++) {
            ArrayUtils.remove(args, i);
        }

        this.invoke(found, args, sender);
        return false;
    }
}
