package me.blazingtide.zetsu;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.blazingtide.zetsu.adapters.ParameterAdapter;
import me.blazingtide.zetsu.adapters.defaults.*;
import me.blazingtide.zetsu.permissible.PermissibleAttachment;
import me.blazingtide.zetsu.permissible.impl.permissible.BukkitPermissionAttachment;
import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.processor.bukkit.BukkitCommand;
import me.blazingtide.zetsu.processor.impl.SpigotProcessor;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Zetsu {
    public static String CMD_SPLITTER = " "; //Splitter for commands / arguments

    //Storing labels && commands associated with the label is faster than looping through all of the labels for no reason.
    private final Map<String, List<CachedCommand>> labelMap = Maps.newHashMap();
    private final Map<Class<?>, ParameterAdapter<?>> parameterAdapters = Maps.newConcurrentMap(); //Multithreading :D
    private final Map<Class<? extends Annotation>, PermissibleAttachment<? extends Annotation>> permissibleAttachments = Maps.newConcurrentMap();
    private final SpigotProcessor processor = new SpigotProcessor(this);
    private final TabCompleteHandler tabCompleteHandler = new TabCompleteHandler(this);
    private CommandMap commandMap = getCommandMap();

    private final JavaPlugin plugin;

    @Setter
    private String fallbackPrefix = "zetsu";

    public Zetsu(JavaPlugin plugin) {
        this.plugin = plugin;

        registerParameterAdapter(String.class, new StringTypeAdapter());
        registerParameterAdapter(Player.class, new PlayerTypeAdapter());
        registerParameterAdapter(Integer.class, new IntegerTypeAdapter());
        registerParameterAdapter(Double.class, new DoubleTypeAdapter());
        registerParameterAdapter(Boolean.class, new BooleanTypeAdapter());

        registerPermissibleAttachment(Permissible.class, new BukkitPermissionAttachment());
    }

    public void registerCommands(Object... objects) {
        for (Object object : objects) {
            registerCommand(object);
        }
    }

    public <T> void registerParameterAdapter(Class<T> clazz, ParameterAdapter<T> adapter) {
        parameterAdapters.putIfAbsent(clazz, adapter);
    }

    public <T extends Annotation> void registerPermissibleAttachment(Class<T> clazz, PermissibleAttachment<T> attachment) {
        permissibleAttachments.putIfAbsent(clazz, attachment);
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
                    BukkitCommand bukkitCommand = new BukkitCommand(command.getLabel(), processor, tabCompleteHandler);
                    bukkitCommand.setDescription(command.getDescription());

                    commandMap.register(fallbackPrefix, bukkitCommand);
                }

                labelMap.putIfAbsent(command.getLabel(), new ArrayList<>());
                labelMap.get(command.getLabel()).add(command);
                labelMap.get(command.getLabel()).sort((o1, o2) -> o2.getMethod().getParameterCount() - o1.getMethod().getParameterCount());
            }
        }
    }

    private CommandMap getCommandMap() {
        final PluginManager manager = Bukkit.getPluginManager();

        try {
            Field field = manager.getClass().getDeclaredField("commandMap");

            field.setAccessible(true);

            return (CommandMap) field.get(manager);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

}
