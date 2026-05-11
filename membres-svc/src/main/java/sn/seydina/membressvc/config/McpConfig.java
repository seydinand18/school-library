package sn.seydina.membressvc.config;

import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.seydina.membressvc.controller.MembreController;

@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider membreToolsProvider(MembreController membreController) {
        return MethodToolCallbackProvider.builder().toolObjects(membreController).build();
    }
}
