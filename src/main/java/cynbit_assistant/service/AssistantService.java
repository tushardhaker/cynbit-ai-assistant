package cynbit_assistant.service;

import cynbit_assistant.dto.ChatRequest;
import cynbit_assistant.dto.ChatResponse;
import cynbit_assistant.entity.ClientLead;
import cynbit_assistant.repository.ClientLeadRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

@Service
public class AssistantService {

    private final ClientLeadRepository leadRepository;
    private final HttpClient httpClient;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    private static final String SYSTEM_PROMPT = 
        "You are Cynbit AI, the enterprise consultant for Cynbit Technologies. " +
        "Cynbit builds scalable digital platforms, AI automation systems, and high-performance engineering labs. " +
        "Reply in 2 short professional sentences. If asked about pricing or quotes, give a benchmark of 4 to 12 weeks and invite them to leave details.";

    public AssistantService(ClientLeadRepository leadRepository) {
        this.leadRepository = leadRepository;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public ChatResponse processChat(ChatRequest request) {
        if (request.getLeadEmail() != null && !request.getLeadEmail().trim().isEmpty()) {
            ClientLead lead = new ClientLead(
                (request.getLeadName() != null && !request.getLeadName().trim().isEmpty())
                    ? request.getLeadName()
                    : "Prospective Client",
                request.getLeadEmail().trim(),
                "Website AI Assistant Lead"
            );
            leadRepository.save(lead);

            return new ChatResponse(
                "Thank you, " + lead.getFullName() + "! Your inquiry is saved. A Cynbit solution architect will contact you within 24 hours.",
                "LEAD_SAVED",
                Collections.emptyList()
            );
        }

        String userMsg = request.getMessage() != null ? request.getMessage().trim() : "";
        if (userMsg.isEmpty()) {
            return new ChatResponse("How can Cynbit assist your AI and platform roadmap today?", "NORMAL", Arrays.asList("AI Automation", "Platform Engineering", "Request a Quote"));
        }

        String lower = userMsg.toLowerCase();
        boolean isQuoteIntent = lower.contains("quote") || lower.contains("pricing") || lower.contains("cost");

        String aiReply = callGroqLlm(userMsg);

        return new ChatResponse(
            aiReply,
            isQuoteIntent ? "PROMPT_LEAD" : "NORMAL",
            Arrays.asList("Explore AI Systems", "Platform Scalability", "Request a Quote")
        );
    }

    private String callGroqLlm(String prompt) {
        try {
            String sanitizedPrompt = prompt.replace("\"", "\\\"").replace("\n", " ");
            String sanitizedSystem = SYSTEM_PROMPT.replace("\"", "\\\"");

            String payload = "{"
                    + "\"model\":\"" + model + "\","
                    + "\"messages\":["
                    + "{\"role\":\"system\",\"content\":\"" + sanitizedSystem + "\"},"
                    + "{\"role\":\"user\",\"content\":\"" + sanitizedPrompt + "\"}"
                    + "],"
                    + "\"max_tokens\":180,"
                    + "\"temperature\":0.7"
                    + "}";

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey.trim())
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() == 200) {
                String body = resp.body();
                int contentIndex = body.indexOf("\"content\":");
                if (contentIndex != -1) {
                    int start = body.indexOf("\"", contentIndex + 10) + 1;
                    int end = body.indexOf("\"", start);
                    while (end != -1 && body.charAt(end - 1) == '\\') {
                        end = body.indexOf("\"", end + 1);
                    }
                    if (start != 0 && end != -1) {
                        return body.substring(start, end).replace("\\n", " ").replace("\\\"", "\"");
                    }
                }
            } else {
                System.err.println("Groq API returned HTTP " + resp.statusCode() + ": " + resp.body());
            }
        } catch (Exception e) {
            System.err.println("Groq Connection Error: " + e.getMessage());
        }

        return "Cynbit Technologies specializes in engineering AI automation systems and high-throughput cloud platforms. What specific requirement is your team evaluating?";
    }
}