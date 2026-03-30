package edu.autonoma.turboclash.validation;

public class IpValidator {

    public static void validate(String ip) throws InvalidIpException {

        if (ip == null || ip.isEmpty()) {
            throw new InvalidIpException("La dirección IP no puede estar vacía");
        }

        String regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}$";

        if (!ip.matches(regex)) {
            throw new InvalidIpException("El formato de la dirección IP es inválido");
        }
    }
}