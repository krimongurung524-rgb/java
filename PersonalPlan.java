/**
 * PersonalPlan class extends AIModel and represents a personal subscription plan
 * for recreational users with monthly prompt quota limits.
 *
 * @author Krimon Gurung
 * @version 2.0 (Milestone 2)
 */
public class PersonalPlan extends AIModel {

    private int promptsRemaining;

    public PersonalPlan(String modelName, double price, int parameterCount,
                        int contextWindow, int promptsRemaining) {
        super(modelName, price, parameterCount, contextWindow);
        this.promptsRemaining = promptsRemaining;
    }

    public int getPromptsRemaining() { return promptsRemaining; }

    /**
     * Allows users to purchase additional prompts.
     * Validates that amount is positive before adding to quota.
     */
    public String purchasePrompts(int amount) {
        if (amount <= 0) {
            return "Error: Purchase amount must be positive.";
        }
        promptsRemaining += amount;
        return "Successfully purchased " + amount + " prompts. " +
               "Total prompts remaining: " + promptsRemaining;
    }

    /**
     * Executes a prompt with dual validation (quota + context window).
     * Overrides abstract usePrompt from AIModel.
     */
    @Override
    public String usePrompt(String promptText, int outputTokens) {
        if (promptsRemaining <= 0) {
            return "Error: Monthly quota exhausted. Please purchase additional prompts.";
        }
        boolean isValid = calculateTokenUsage(promptText, outputTokens);
        if (!isValid) {
            return "Error: Context limit exceeded. " +
                   "The total tokens (Input + Output + System) exceed the context window of " +
                   getContextWindow() + " tokens.";
        }
        promptsRemaining--;
        return "Prompt executed successfully!\n" +
               "Prompt: " + promptText + "\n" +
               "Output Tokens: " + outputTokens + "\n" +
               "Monthly Prompts Remaining: " + promptsRemaining;
    }

    @Override
    public String display() {
        return "=== Personal Plan ===\n" +
               "Model Name: " + getModelName() + "\n" +
               "Price: NPR " + getPrice() + " per 1 Lakh tokens\n" +
               "Parameter Count: " + getParameterCount() + " billion\n" +
               "Context Window: " + getContextWindow() + " tokens\n" +
               "Monthly Quota Remaining: " + promptsRemaining + " prompts";
    }
}