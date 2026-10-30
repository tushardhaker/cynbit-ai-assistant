package cynbit_assistant.dto;

import java.util.List;

public class ChatResponse {
    private String reply;
    private String action;
    private List<String> suggestedActions;

    public ChatResponse(String reply, String action, List<String> suggestedActions) {
        this.reply = reply;
        this.action = action;
        this.suggestedActions = suggestedActions;
    }

    public String getReply() {
        return reply;
    }

    public String getAction() {
        return action;
    }

    public List<String> getSuggestedActions() {
        return suggestedActions;
    }
}