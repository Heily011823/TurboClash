package edu.autonoma.turboclash;

/**
 * Representa la clase `ManualTestRunner` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public final class ManualTestRunner {

    private ManualTestRunner() {
    }

    /**
     * Ejecuta la operacion publica `main`.
     * @param args valor del parametro `args`
     */
    public static void main(String[] args) {
        GameRulesManagerTest.run();
        CollisionManagerTest.run();
        MessageCodecTest.run();
        MatchSequenceTest.run();
        System.out.println("Manual tests passed.");
    }
}
