package util;

public class ScenarioContext {
    private static final ThreadLocal<String> scenarioName = new ThreadLocal<>();
    private static final ThreadLocal<String> currentStep = new ThreadLocal<>();

    public static void setScenarioName(String name) {
        scenarioName.set(name);
    }

    public static String getScenarioName() {
        return scenarioName.get();
    }

    public static void clearScenarioName() {
        scenarioName.remove();
    }

    public static void setCurrentStep(String step) {
        currentStep.set(step);
    }

    public static String getCurrentStep() {
        return currentStep.get();
    }

    public static void clearCurrentStep() {
        currentStep.remove();
    }

    public static void clearAll() {
        clearCurrentStep();
        clearScenarioName();
    }
}
