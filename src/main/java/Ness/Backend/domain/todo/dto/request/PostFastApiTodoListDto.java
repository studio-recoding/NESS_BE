package Ness.Backend.domain.todo.dto.request;

import Ness.Backend.domain.profile.entity.PersonaType;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonProperty("persona")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PersonaType persona;

    @JsonProperty("todoList")
    private List<PostFastApiTodoDto> todoList;
}
