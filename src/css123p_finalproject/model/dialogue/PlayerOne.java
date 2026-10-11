package css123p_finalproject.model.dialogue;

public class PlayerOne {
    
    String[] DefaultDialogues = {
    "Princess, the Final Boss has fallen and the kingdom is safe.",
    "It is my utmost desire to protect the people.",
    "I would like to honor the fallen knights before me."
    };

    String DecisionAcceptAll = "I accept your offer, we shall all be bound together with love.";
    String DecisionRejectAll = "I cannot accept your offers, my mission must be prioritized.";
    String DecisionAcceptOne = "I would devote myself to ";
    String DecisionRejectOne = "Unfortunately, ";

    PlayerOne() {}

    public String Talk() {
        return "";
    }
    public String Decide() {return "";}
    public String Accept(String PlayerInput, String Choice) { return "";}
}