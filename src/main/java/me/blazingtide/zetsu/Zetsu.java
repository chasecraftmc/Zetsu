package me.blazingtide.zetsu;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.blazingtide.zetsu.adapters.ParameterAdapter;
import me.blazingtide.zetsu.processor.bukkit.BukkitCommand;
import me.blazingtide.zetsu.processor.impl.SpigotProcessor;
import me.blazingtide.zetsu.schema.annotations.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class Zetsu {

    public static String CMD_SPLITTER = " "; //Splitter for commands / arguments

    private final Set<CachedCommand> commands = Sets.newConcurrentHashSet();
    //Storing labels && commands associated with the label is faster than looping through all of the labels for no reason.
    private final Map<String, List<CachedCommand>> labelMap = Maps.newHashMap();
    private final Map<Class<?>, ParameterAdapter<?>> parameterAdapters = Maps.newConcurrentMap(); //Multithreading :D
    private final SpigotProcessor processor = new SpigotProcessor(this);
    private CommandMap commandMap = getCommandMap();

    private final JavaPlugin plugin;

    public Zetsu(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerCommands(Object... objects) {
        for (Object object : objects) {
            registerCommand(object);
        }
    }

    public <T> void registerParameterAdapter(Class<T> clazz, ParameterAdapter<T> adapter) {
        parameterAdapters.putIfAbsent(clazz, adapter);
    }

    private void registerCommand(Object object) {
        if (commandMap == null) {
            commandMap = getCommandMap();
        }

        for (Method method : object.getClass().getMethods()) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            List<CachedCommand> commands = CachedCommand.of(method.getAnnotation(Command.class), method, object);

            for (CachedCommand command : commands) {
                org.bukkit.command.Command cmd = commandMap.getCommand(command.getLabel());

                if (cmd == null) {
                    BukkitCommand bukkitCommand = new BukkitCommand(command.getLabel(), processor);
                    bukkitCommand.setDescription(command.getDescription());

                    commandMap.register("zetsu", bukkitCommand);
                }

                labelMap.putIfAbsent(command.getLabel(), new ArrayList<>());
                labelMap.get(command.getLabel()).addAll(commands);

                this.commands.addAll(commands);
            }
        }
    }

    private CommandMap getCommandMap() {
        final PluginManager manager = Bukkit.getPluginManager();

        if (manager instanceof SimplePluginManager) {
            try {
                Field field = manager.getClass().getDeclaredField("commandMap");

                field.setAccessible(true);

                return (CommandMap) field.get(manager);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
