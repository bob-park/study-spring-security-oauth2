package com.example.oauth2resourceserver.configure.custom;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

public class CustomKeyValueCondition extends SpringBootCondition {


    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition("Custom Key Value Condition");

        Environment environment = context.getEnvironment();

        String customStr = environment.getProperty("custom.custom-str");

        if (!StringUtils.hasText(customStr)) {
            return ConditionOutcome.noMatch(message.didNotFind("custom-str property").atAll());
        }

        return ConditionOutcome.match(message.foundExactly("custom str property"));
    }
}
