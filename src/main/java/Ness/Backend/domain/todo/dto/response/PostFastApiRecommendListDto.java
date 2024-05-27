package Ness.Backend.domain.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFastApiRecommendListDto {
    @JsonProperty("recommendationList")
    List<PostFastApiRecommendDto> recommendationList;
}
