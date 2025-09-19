package com.nomiceu.nomilabs.mixin.groovyscript;

import net.prominic.groovyls.providers.CompletionProvider;

import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.cleanroommc.groovyscript.mapper.AbstractObjectMapper;
import com.cleanroommc.groovyscript.server.CompletionParams;
import com.cleanroommc.groovyscript.server.Completions;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.groovy.mixinhelper.FilterableObjectMapper;

/**
 * Filters object mapper completions by existing string, to avoid our own set limit of 1000.
 */
@Mixin(value = CompletionProvider.class, remap = false)
public class CompletionProviderMixin {

    @Redirect(method = "populateItemsFromConstantExpression",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/groovyscript/mapper/AbstractObjectMapper;provideCompletion(ILcom/cleanroommc/groovyscript/server/CompletionParams;Lcom/cleanroommc/groovyscript/server/Completions;)V"),
              require = 1)
    private void useFilteredCompletions(AbstractObjectMapper<?> instance, int index, CompletionParams params,
                                        Completions items,
                                        @Local(argsOnly = true, ordinal = 0) ConstantExpression node) {
        // From previous type class checks, we know value (of node) is not null and is string, so just call getText()
        ((FilterableObjectMapper) instance).labs$provideCompletion(index, params, items, node.getText());
    }
}
