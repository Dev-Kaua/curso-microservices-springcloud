package io.github.Dev_Kaua.msCloudGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
@EnableDiscoveryClient
public class MsCloudGatewayApplication {

	public static void main(String[] args) {

		SpringApplication.run(MsCloudGatewayApplication.class, args);
	}

	public RouteLocator routes(RouteLocatorBuilder builder){
		return builder
				.routes()
					.route(r -> r.path("/clientes/**").uri("lb://msclientes"))
					.route(r -> r.path("/cartoes/**").uri("lb://mscartoes"))
				.build();
	}

}
