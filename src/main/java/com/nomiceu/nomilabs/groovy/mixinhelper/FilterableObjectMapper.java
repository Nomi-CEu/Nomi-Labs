package com.nomiceu.nomilabs.groovy.mixinhelper;

import com.cleanroommc.groovyscript.server.CompletionParams;
import com.cleanroommc.groovyscript.server.Completions;

public interface FilterableObjectMapper {

    void labs$provideCompletion(int index, CompletionParams params, Completions items, String existing);
}
