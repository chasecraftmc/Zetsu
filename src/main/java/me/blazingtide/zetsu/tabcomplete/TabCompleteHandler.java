package me.blazingtide.zetsu.tabcomplete;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.schema.CachedCommand;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

@AllArgsConstructor
public class TabCompleteHandler {

    private final Zetsu zetsu;

    public Set<String> request(String command) {
        return Sets.newHashSet();
    }

    public List<String> requestSubcommands(String label) {
        return zetsu.getLabelMap().getOrDefault(label, Lists.newArrayList()).stream().map(new Function<CachedCommand, String>() {
            @Override
            public String apply(CachedCommand cachedCommand) {
                return cachedCommand.getLabel().replace(label, "");
            }
        });
    }

}
