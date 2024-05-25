package Ness.Backend.domain.category.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCategoryDto {
    @JsonProperty("category")
    private String name;

    @JsonProperty("categoryColor")
    private String color;
}
