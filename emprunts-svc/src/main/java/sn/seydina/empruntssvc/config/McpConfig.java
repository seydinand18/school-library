package sn.seydina.empruntssvc.config;

import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.seydina.empruntssvc.controller.EmpruntController;

@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider empruntToolsProvider(EmpruntController empruntController) {
        return MethodToolCallbackProvider.builder().toolObjects(empruntController).build();
    }
}
