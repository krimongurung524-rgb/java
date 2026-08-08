/**
 * AIModel is an abstract parent class representing an AI subscription model.
 * It centralizes token calculation logic to prevent code duplication.
 *
 * @author Krimon Gurung
 * @version 2.0 (Milestone 2)
 */
public abstract class AIModel implements java.io.Serializable {

    private String modelName;
    private double price;
    private int parameterCount;
    private int contextWindow;

    private static final int SYSTEM_TOKENS = 50;

    public AIModel(String modelName, double price, int parameterCount, int contextWindow) {
        this.modelName      = modelName;
        this.price          = price;
        this.parameterCount = parameterCount;
        this.contextWindow  = contextWindow;
    }

    public String getModelName()    { return modelName; }
    public double getPrice()         { return price; }
    public int    getParameterCount(){ return parameterCount; }
    public int    getContextWindow() { return contextWindow; }

    /**
     * Calculates token usage and validates against context window.
     * inputTokens = ceil(promptText.length / 4.0) + outputTokens + SYSTEM_TOKENS (50)
     *
     * @param promptText   the input prompt text
     * @param outputTokens expected output token count
     * @return true if total tokens <= contextWindow, false otherwise
     */
    public boolean calculateTokenUsage(String promptText, int outputTokens) {
        int inputTokens  = (int) Math.ceil(promptText.length() / 4.0);
        int totalTokens  = inputTokens + outputTokens + SYSTEM_TOKENS;
        if (totalTokens <= contextWindow) {
            return true;
        } else {
            return false;
        }
    }

    public abstract String display();
    public abstract String usePrompt(String promptText, int outputTokens);
}