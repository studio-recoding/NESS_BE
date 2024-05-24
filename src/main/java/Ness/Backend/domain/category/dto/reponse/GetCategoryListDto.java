package Ness.Backend.domain.category.dto.reponse;

import Ness.Backend.domain.chat.dto.response.GetChatDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCategoryListDto {
    private List<GetCategoryDto> categoryList;
}
