package cynbit_assistant.service;

import cynbit_assistant.dto.ChatRequest;
import cynbit_assistant.dto.ChatResponse;
import cynbit_assistant.entity.ClientLead;
import cynbit_assistant.repository.ClientLeadRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
public class AssistantService {

    private final ClientLeadRepository leadRepository;

    public AssistantService(ClientLeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public ChatResponse processChat(ChatRequest request) {
        if (request.getLeadEmail() != null && !request.getLeadEmail().trim().isEmpty()) {
            ClientLead lead = new ClientLead(
                (request.getLeadName() != null && !request.getLeadName().trim().isEmpty()) 
                    ? request.getLeadName() 
                    : "Prospective Client",
                request.getLeadEmail().trim(),
                "Website AI Assistant Inquiry"
            );
            leadRepository.save(lead);

            return new ChatResponse(
                "Thank you, " + lead.getFullName() + "! Your inquiry has been logged in our system. A Cynbit solution architect will contact you within 24 hours.",
                "LEAD_SAVED",
                Collections.emptyList()
            );
        }

        String msg = (request.getMessage() != null) ? request.getMessage().toLowerCase() : "";

        if (msg.contains("ai") || msg.contains("agent") || msg.contains("automation")) {
            return new ChatResponse(
                "Cynbit architects autonomous AI agents, enterprise workflow automation, and proprietary retrieval engines. Would you like to schedule an AI discovery audit?",
                "NORMAL",
                Arrays.asList("Schedule AI Audit", "Cost & Timeline", "Request a Quote")
            );
        }

        if (msg.contains("platform") || msg.contains("web") || msg.contains("full stack") || msg.contains("saas")) {
            return new ChatResponse(
                "We engineer resilient web platforms, microservices backends, and cloud-native systems designed for scale. What scale or tech requirements are you targeting?",
                "NORMAL",
                Arrays.asList("MVP in 6 Weeks", "Enterprise Scalability", "Request a Quote")
            );
        }

        if (msg.contains("cost") || msg.contains("price") || msg.contains("quote") || msg.contains("timeline")) {
            return new ChatResponse(
                "Typical delivery cycles run 4 to 12 weeks. Share your name and email so our engineering leads can generate an itemized quote.",
                "PROMPT_LEAD",
                Collections.emptyList()
            );
        }

        return new ChatResponse(
            "Welcome to Cynbit Technologies. I can help explore our AI Automation, Platform Engineering, or estimate a timeline. How can we help your business today?",
            "NORMAL",
            Arrays.asList("Explore AI Automation", "Platform Engineering", "Request a Quote")
        );
    }
}