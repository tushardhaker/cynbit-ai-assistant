package cynbit_assistant.controller;

import cynbit_assistant.dto.ChatRequest;
import cynbit_assistant.dto.ChatResponse;
import cynbit_assistant.service.AssistantService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final AssistantService assistantService;

    public WebController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/api/chat")
    @ResponseBody
    public ResponseEntity<ChatResponse> handleChat(@RequestBody ChatRequest request) {
        ChatResponse response = assistantService.processChat(request);
        return ResponseEntity.ok(response);
    }
}