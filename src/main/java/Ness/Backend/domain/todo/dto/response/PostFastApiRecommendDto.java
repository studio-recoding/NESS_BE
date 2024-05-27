package Ness.Backend.domain.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFastApiRecommendDto {
    @JsonProperty("todo")
    private PostFastApiRecommendTodoDto todo;

    @JsonProperty("nessComment")
    private String nessComment;
}
