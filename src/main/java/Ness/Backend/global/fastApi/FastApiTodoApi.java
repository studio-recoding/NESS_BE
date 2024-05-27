package Ness.Backend.global.fastApi;

import Ness.Backend.domain.todo.dto.request.PostFastApiTodoListDto;
import Ness.Backend.domain.todo.dto.response.PostFastApiRecommendListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(
        name = "FastApiTodo",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
public interface FastApiTodoApi {
    @PostMapping(value = "/recommendation/list")
    PostFastApiRecommendListDto creatFastApiTodo(PostFastApiTodoListDto postFastApiTodoListDto);
}