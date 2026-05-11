package sn.seydina.livressvc.config;

import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.seydina.livressvc.controller.LivreController;

@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider livreToolsProvider(LivreController livreController) {
        return MethodToolCallbackProvider.builder().toolObjects(livreController).build();
    }
}
