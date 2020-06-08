package me.blazingtide.zetsu.command.processor;

import lombok.AllArgsConstructor;
import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.command.CachedCommand;
import me.blazingtide.zetsu.command.adapters.ParameterAdapter;
import me.blazingtide.zetsu.command.schema.Permissible;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@AllArgsConstructor
public class CommandProcessor {

    private final Zetsu zetsu;

    public CachedCommand find(String label, String[] args, CommandSender sender) {
        final List<CachedCommand> cmds = zetsu.getLabelMap().getOrDefault(label, null);

        Finder:
        for (CachedCommand cmd : cmds) {
            int paramCount = cmd.getMethod().getParameterCount();

            for (int i = 0; i < Math.min(args.length - paramCount, 0); i++) {
                if (!cmd.getArgs().get(i).equalsIgnoreCase(args[i])) {
                    continue Finder;
                }
            }

            return cmd;
        }

        return null;
    }

    //The args is for the parameter, IGNORE the subcommand type args
    public void invoke(CachedCommand command, String[] args, CommandSender sender) {
        final Runnable runnable = () -> {
            final Method method = command.getMethod();
            final Object[] objects = new Object[method.getParameterCount()];

            if (method.getParameterCount() <= 0) {
                sender.sendMessage(ChatColor.RED + "This command is incorrectly setup! Please fix immediately. (Error: Invalid amount of method parameters)");
                return;
            }

            if (method.isAnnotationPresent(Permissible.class) && !sender.hasPermission(method.getAnnotation(Permissible.class).value())) {
                //default mc no perm message :D
                sender.sendMessage(ChatColor.RED + "&cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
                return;
            }

            if (method.getParameters()[0].getType() == Player.class && !(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be ran by players.");
                return;
            }

            objects[0] = sender; //The first parameter is always the sender

            for (int i = 0; i < args.length; i++) {
                if (!zetsu.getParameterAdapters().containsKey(method.getParameters()[i].getType())) {
                    sender.sendMessage(ChatColor.RED + "This command is incorrectly setup! Please fix immediately. (Error: Parameter Type does not have an adapter)");
                    return;
                }

                final ParameterAdapter<?> adapter = zetsu.getParameterAdapters().get(method.getParameters()[i].getType());

                try {
                    objects[i + 1] = adapter.process(args[i]);
                } catch (Exception e) {
                    adapter.processException(sender, args[i], e);
                    return;
                }
            }

            try {
                method.invoke(command.getObject(), objects);
            } catch (IllegalAccessException | InvocationTargetException e) {
                sender.sendMessage(ChatColor.RED + "An error occurred while processing this command. Please contact a developer and report this as a bug.");
                e.printStackTrace();
            }
        };

        if (command.isAsync()) {
            Bukkit.getScheduler().runTaskAsynchronously(zetsu.getPlugin(), runnable);
        } else {
            runnable.run();
        }
    }

}
