package me.blazingtide.zetsu.processor.impl;

import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.processor.CommandProcessor;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.blazingtide.zetsu.schema.annotations.parameter.Default;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class SpigotProcessor extends CommandProcessor implements CommandExecutor {

    public SpigotProcessor(Zetsu zetsu) {
        super(zetsu);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final CachedCommand found = this.find(label, args);

        if (found == null) {
            sendHelpMessage(label, sender);
//            sender.sendMessage(ChatColor.RED + "Library Error. (Command not found)");
            return false;
        }

        int defaults = 0;

        for (Parameter parameter : found.getMethod().getParameters()) {
            defaults += parameter.isAnnotationPresent(Default.class) ? 1 : 0;
        }

        for (int i = 0; i < found.getArgs().size() + defaults; i++) {
            if (args.length > i) {
                args = (String[]) ArrayUtils.remove(args, 0);
            }
        }

        this.invoke(found, args, sender);
        return false;
    }

    private void sendHelpMessage(String label, CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------");
        sender.sendMessage(ChatColor.GOLD + StringUtils.capitalize(label) + ChatColor.GRAY + " -" + ChatColor.WHITE + " (Command Help)");
        sender.sendMessage(" ");

        for (CachedCommand command : zetsu.getLabelMap().get(label)) {
            final Method method = command.getMethod();
            final StringBuilder builder = new StringBuilder();

            for (int i = 1; i < method.getParameters().length; i++) {
                final Parameter parameter = method.getParameters()[i];

                if (parameter.isAnnotationPresent(Param.class)) {
                    builder.append("<").append(parameter.getAnnotation(Param.class).value()).append("> ");
                } else {
                    builder.append("<").append(parameter.getType().getSimpleName()).append("> ");
                }
            }

            sender.sendMessage(" " + ChatColor.YELLOW + "/" + label + " " + String.join(" ", command.getArgs()) + " " + builder.toString().trim() + ChatColor.GRAY + " - " + ChatColor.WHITE + command.getDescription());
        }

        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GOLD + "You're on page " + ChatColor.WHITE + "1" + ChatColor.GOLD + " of " + ChatColor.WHITE + "1" + ChatColor.GOLD + ".");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------");
    }

}
