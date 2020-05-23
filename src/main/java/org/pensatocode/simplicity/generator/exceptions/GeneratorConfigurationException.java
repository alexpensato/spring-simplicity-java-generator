package org.pensatocode.simplicity.generator.exceptions;

public class GeneratorConfigurationException extends Exception {

    public GeneratorConfigurationException(String msg, Object... args) {
        super(String.format(msg, args));
    }
}
