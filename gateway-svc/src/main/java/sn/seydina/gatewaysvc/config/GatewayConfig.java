package sn.seydina.gatewaysvc.config;

import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    // Routes dynamiques via Eureka — équivalent de discovery.locator.enabled: true dans le YAML
//    @Bean
//    public DiscoveryClientRouteDefinitionLocator discoveryRoutes(ReactiveDiscoveryClient rdc, DiscoveryLocatorProperties dp) {
//        return new DiscoveryClientRouteDefinitionLocator(rdc, dp);
//    }

    // Routes statiques — décommenter pour la démo des routes manuelles
//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("membres-svc", r -> r.path("/membres/**")
//                        .uri("lb://membres-svc"))
//                .route("livres-svc", r -> r.path("/livres/**")
//                        .uri("lb://livres-svc"))
//                .route("emprunts-svc", r -> r.path("/emprunts/**")
//                        .uri("lb://emprunts-svc"))
//                .build();
//    }
}
