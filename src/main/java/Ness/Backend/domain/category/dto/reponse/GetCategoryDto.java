package Ness.Backend.domain.category.dto.reponse;

import Ness.Backend.domain.category.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetCategoryDto {
    @JsonProperty("categoryNum")
    private Long id;

    @JsonProperty("category")
    private String name;

    @JsonProperty("categoryColor")
    private String color;
}
