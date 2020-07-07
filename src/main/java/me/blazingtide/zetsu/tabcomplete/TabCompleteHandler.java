package me.blazingtide.zetsu.tabcomplete;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.tabcomplete.listener.TabCompleteListener;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TabCompleteHandler {

    private final Map<String, List<String>> subcommandCache = Maps.newHashMap(); //Instead of looping every time, we store the sub commands and loop ONCE. (under the impression sub commands won't be created mid runtime)

    private final Zetsu zetsu;

    @Getter
    private final TabCompleteListener listener;

    public TabCompleteHandler(Zetsu zetsu) {
        this.zetsu = zetsu;

        this.listener = new TabCompleteListener(this, zetsu.getProcessor());
    }

    public Set<String> request(String command) {
        return Sets.newHashSet();
    }

    public List<String> requestSubcommands(String label) {
        //very slow because if we have ~100 commands registered, it's going to do a loop every tab complete = lags the server...
        // even tho it's barely noticeable, it will add up with a lot of players

//        return zetsu.getLabelMap().getOrDefault(label, Lists.newArrayList()).stream().map(new Function<CachedCommand, String>() {
//            @Override
//            public String apply(CachedCommand cachedCommand) {
//                return cachedCommand.getLabel().replace(label, "");
//            }
//        });

        subcommandCache.computeIfAbsent(label, string -> zetsu.getLabelMap().get(string).stream().map(command -> {
            final String[] split = command.getLabel().split(Zetsu.CMD_SPLITTER);
            if (split.length == 0) {
                return null;
            }

            return split[split.length - 1];
        }).filter(Objects::nonNull).collect(Collectors.toList()));

        return subcommandCache.get(label);
    }

}
