package com.bersyte.eventz.common.infrastructure.utils;

import com.bersyte.eventz.common.application.services.ThrowableAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SafeActionExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SafeActionExecutor.class);
    
    public void safeExecute(ThrowableAction action, String context, String target) {
        try {
            action.execute();
        } catch (Exception e) {
            logger.error("SAFE ACTION ERROR - Context: [{}], Target: [{}], Message: [{}]", context, target, e.getMessage());
        }
    }
}
