package org.pensatocode.simplicity.generator.components;

import java.util.Set;

public interface Config {
    String getApiContext();
    Set<String> getRegenerateControllers();
    Set<String> getRegenerateRepositories();
    Set<String> getRegenerateMappers();
}
