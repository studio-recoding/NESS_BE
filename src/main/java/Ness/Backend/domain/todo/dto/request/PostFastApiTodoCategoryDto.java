package Ness.Backend.domain.todo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFastApiTodoCategoryDto {
    @JsonProperty("categoryId")
    private Long id;

    @JsonProperty("categoryName")
    private String name;

    @JsonProperty("categoryColor")
    private String color;
}
