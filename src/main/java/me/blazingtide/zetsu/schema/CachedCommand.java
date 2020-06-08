package me.blazingtide.zetsu.schema;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.schema.annotations.Command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class CachedCommand {

    private final String label;
    private final List<String> args;

    private final String description;
    private final boolean async;
    private final Method method;
    private final Object object;

    //TODO: Add default parameters
    public static List<CachedCommand> of(Command annotation, Method method, Object object) {
        final List<CachedCommand> commands = Lists.newArrayList();

        for (String label : annotation.labels()) {
            final String[] split = label.split(Zetsu.CMD_SPLITTER);

            commands.add(new CachedCommand(split[0], Arrays.asList(split).subList(1, split.length), annotation.description(), annotation.async(), method, object));
        }

        return commands;
    }

    @Override
    public String toString() {
        return "CachedCommand{" +
                "label='" + label + '\'' +
                ", args=" + args +
                ", description='" + description + '\'' +
                ", async=" + async +
                ", method=" + method +
                ", object=" + object +
                '}';
    }
}
