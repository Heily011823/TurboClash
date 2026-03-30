package edu.autonoma.turboclash.validation;

/**
 * Clase encargada de validar direcciones IP.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.validation.IpValidator
 * @since 2026-03-29
 */
public class IpValidator {

    /**
     * Valida una dirección IP.
     *
     * @param ip Es la dirección IP a validar.
     * @throws InvalidIpException Se lanza la excepción si la IP es nula, vacía o tiene un formato incorrecto.
     */
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