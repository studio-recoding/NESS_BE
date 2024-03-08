package Ness.Backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /*Info 객체를 생성하여 API 문서의 기본 정보를 설정
    * 기본 정보: API 문서의 제목, 버전, 설명*/
    private Info apiInfo() {
        return new Info()
                .title("NESS Api Docs")
                .version("0.0.1")
                .description("NESS Api 문서입니다");
    }

    /*OpenAPI 객체를 생성하여 API 문서의 전반적인 구성을 설정
    * 앞서 설정한 Info 객체를 지정*/
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

}