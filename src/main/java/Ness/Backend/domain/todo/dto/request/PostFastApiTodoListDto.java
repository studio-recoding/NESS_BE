package Ness.Backend.domain.todo.dto.request;

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
public class PostFastApiTodoListDto {
    @JsonProperty("todoList")
    List<PostFastApiTodoDto> todoList;
}
