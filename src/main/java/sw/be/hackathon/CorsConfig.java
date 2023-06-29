package sw.be.hackathon;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
	@Bean
	public CorsFilter corsFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		//서버가 응답할 때 json을 클라이언트에서 처리할 수 있게 할 지 정하는것
		config.setAllowCredentials(true);
		//모든 ip에 응답 허용
		config.setAllowedOriginPatterns(Collections.singletonList("*"));
		//모든 메서드에 허용
		config.addAllowedMethod("*");
		//모든 header에 응답 허용
		config.addAllowedHeader("*");
		//모든 http method 요청 허용
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
