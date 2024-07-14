package Ness.Backend.domain.bookmark.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
public class GetBookmarkDto {
    private Long id;

    private String contents;

    private ZonedDateTime datetime;

    private String title;

    private String url;

    @Builder
    public GetBookmarkDto(Long id, String contents, ZonedDateTime datetime, String title, String url){
        this.id = id;
        this.contents = contents;
        this.datetime = datetime;
        this.title = title;
        this.url = url;
    }
}
