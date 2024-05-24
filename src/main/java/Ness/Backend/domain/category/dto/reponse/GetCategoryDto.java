package Ness.Backend.domain.category.dto.reponse;

import Ness.Backend.domain.category.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
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

    @Builder
    public GetCategoryDto(Long id, String name, String color){
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
