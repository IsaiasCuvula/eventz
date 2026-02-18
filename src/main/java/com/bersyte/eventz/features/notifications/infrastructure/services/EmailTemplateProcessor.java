package com.bersyte.eventz.features.notifications.infrastructure.services;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class EmailTemplateProcessor {
    private final TemplateEngine templateEngine;
    
    public EmailTemplateProcessor(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    public String process(String templateName, Map<String, String> variables){
        Context context = new Context();
        if (variables != null){
           variables.forEach(context::setVariable);
        }
        return templateEngine.process("emails/" + templateName, context);
    }
}
