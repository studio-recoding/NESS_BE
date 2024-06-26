package Ness.Backend.domain.todo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFastApiTodoDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("startTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZonedDateTime startTime;

    @JsonProperty("category")
    private PostFastApiTodoCategoryDto category;

    @JsonProperty("person")
    private String person;

    @JsonProperty("location")
    private String location;

    @JsonProperty("info")
    private String info;
}
