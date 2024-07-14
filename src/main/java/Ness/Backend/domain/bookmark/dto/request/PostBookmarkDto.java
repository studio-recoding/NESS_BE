package Ness.Backend.domain.bookmark.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class PostBookmarkDto {
    private Long scheduleId;

    private String contents;

    private ZonedDateTime datetime;

    private String title;

    private String url;
}
