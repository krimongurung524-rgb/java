/**
 * ProPlan class extends AIModel for professional team subscriptions.
 * Provides unlimited prompts with team member slot management.
 *
 * @author Krimon Gurung
 * @version 2.0 (Milestone 2)
 */
public class ProPlan extends AIModel {

    private int availableSlots;

    public ProPlan(String modelName, double price, int parameterCount,
                   int contextWindow, int availableSlots) {
        super(modelName, price, parameterCount, contextWindow);
        this.availableSlots = availableSlots;
    }

    public int getAvailableSlots() { return availableSlots; }

    public String addTeamMember(String memberName) {
        if (availableSlots <= 0) {
            return "Error: No team slots available. Cannot add team member.";
        }
        if (memberName == null || memberName.trim().isEmpty()) {
            return "Error: Team member name cannot be empty.";
        }
        availableSlots--;
        return "Team member '" + memberName + "' added successfully. " +
               "Available slots remaining: " + availableSlots;
    }

    public String removeTeamMember(String memberName) {
        if (memberName == null || memberName.trim().isEmpty()) {
            return "Error: Team member name cannot be empty.";
        }
        availableSlots++;
        return "Team member '" + memberName + "' removed successfully. " +
               "Available slots: " + availableSlots;
    }

    /**
     * Executes a prompt with single validation (context window only).
     * No quota check — ProPlan has unlimited prompts.
     */
    @Override
    public String usePrompt(String promptText, int outputTokens) {
        boolean isValid = calculateTokenUsage(promptText, outputTokens);
        if (!isValid) {
            return "Error: Context limit exceeded. " +
                   "The total tokens (Input + Output + System) exceed the context window of " +
                   getContextWindow() + " tokens.";
        }
        return "Prompt executed successfully! (Pro Plan - Unlimited Requests)\n" +
               "Prompt: " + promptText + "\n" +
               "Output Tokens: " + outputTokens + "\n" +
               "Note: Pro Plan has no monthly quota limit.";
    }

    @Override
    public String display() {
        return "=== Pro Plan (Team) ===\n" +
               "Model Name: " + getModelName() + "\n" +
               "Price: NPR " + getPrice() + " per 1 Lakh tokens\n" +
               "Parameter Count: " + getParameterCount() + " billion\n" +
               "Context Window: " + getContextWindow() + " tokens\n" +
               "Available Team Slots: " + availableSlots;
    }
}