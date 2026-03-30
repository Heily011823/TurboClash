package edu.autonoma.turboclash.validation;

public class PortValidator {

    public static void validate(int port) throws InvalidPortException {

        if (port < 1024 || port > 65535) {
            throw new InvalidPortException("El puerto está fuera de rango");
        }
    }
}