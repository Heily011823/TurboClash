package edu.autonoma.turboclash;

public final class ManualTestRunner {

    private ManualTestRunner() {
    }

    public static void main(String[] args) {
        GameRulesManagerTest.run();
        CollisionManagerTest.run();
        MessageCodecTest.run();
        MatchSequenceTest.run();
        System.out.println("Manual tests passed.");
    }
}
