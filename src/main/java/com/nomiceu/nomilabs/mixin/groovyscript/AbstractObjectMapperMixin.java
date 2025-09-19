package com.nomiceu.nomilabs.mixin.groovyscript;

import java.util.Locale;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.cleanroommc.groovyscript.mapper.AbstractObjectMapper;
import com.cleanroommc.groovyscript.server.CompletionParams;
import com.cleanroommc.groovyscript.server.Completions;
import com.nomiceu.nomilabs.groovy.mixinhelper.FilterableObjectMapper;
import com.nomiceu.nomilabs.groovy.mixinhelper.RequiresFiltering;

/**
 * Provides the default implementation of context-aware completions.
 */
@Mixin(value = AbstractObjectMapper.class, remap = false)
public abstract class AbstractObjectMapperMixin implements FilterableObjectMapper {

    @Unique
    private RequiresFiltering labs$requiresFiltering = RequiresFiltering.UNKNOWN;

    @Shadow
    public abstract void provideCompletion(int index, CompletionParams params, Completions items);

    @Unique
    @Override
    public void labs$provideCompletion(int index, CompletionParams params, Completions items, String existing) {
        if (labs$requiresFiltering == RequiresFiltering.NO) {
            provideCompletion(index, params, items);
            return;
        }

        if (labs$requiresFiltering == RequiresFiltering.UNKNOWN) {
            // Test if goes over limit
            var test = new Completions(items.getLimit());
            provideCompletion(index, params, test);

            if (test.reachedLimit())
                labs$requiresFiltering = RequiresFiltering.YES;
            else
                labs$requiresFiltering = RequiresFiltering.NO;

            // Call func again
            labs$provideCompletion(index, params, items, existing);
            return;
        }

        // Use temp to avoid filtering other completions, and to prevent limiting
        var temp = new Completions(Integer.MAX_VALUE);
        provideCompletion(index, params, temp);

        temp.removeIf(completionItem -> !completionItem.getLabel().toLowerCase(Locale.ENGLISH).contains(existing));
        items.addAll(temp, Function.identity());
    }
}
