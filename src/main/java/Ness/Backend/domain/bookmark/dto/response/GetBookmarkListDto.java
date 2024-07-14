package Ness.Backend.domain.bookmark.dto.response;

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
public class GetBookmarkListDto {
    private List<GetBookmarkDto> bookmarkList;
}
