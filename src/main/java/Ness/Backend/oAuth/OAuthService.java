package Ness.Backend.oAuth;

import Ness.Backend.oAuth.dto.GoogleResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthService {
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    public OAuthService(Environment env) {
        this.env = env;
    }
    public GoogleResponseDto socialLogin(String code, String registration) {
        String accessToken = getAccessToken(code, registration);
        JsonNode userResourceNode = getUserResource(accessToken, registration);
        System.out.println("userResourceNode = " + userResourceNode);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("email").asText();
        String picture = userResourceNode.get("picture").asText();

        System.out.println("id = " + id);
        System.out.println("email = " + email);
        System.out.println("picture = " + picture);

        GoogleResponseDto googleResponseDto = GoogleResponseDto.builder()
                .picture(picture)
                .email(email)
                .build();

        return googleResponseDto;
    }

    private String getAccessToken(String authorizationCode, String registration) {
        String clientId = env.getProperty("oauth2." + registration + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registration + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registration + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + registration + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registration) {
        String resourceUri = env.getProperty("oauth2."+registration+".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}
