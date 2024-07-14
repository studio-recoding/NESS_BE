package Ness.Backend.domain.bookmark;

import Ness.Backend.domain.bookmark.dto.request.PostBookmarkDto;
import Ness.Backend.domain.bookmark.dto.response.GetBookmarkDto;
import Ness.Backend.domain.bookmark.dto.response.GetBookmarkListDto;
import Ness.Backend.domain.bookmark.entity.Bookmark;
import Ness.Backend.domain.schedule.ScheduleRepository;
import Ness.Backend.domain.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void createBookmark(PostBookmarkDto postBookmarkDto){
        Schedule schedule = scheduleRepository.findScheduleById(postBookmarkDto.getScheduleId());

        Bookmark bookmark = Bookmark.builder()
                .contents(postBookmarkDto.getContents())
                .title(postBookmarkDto.getTitle())
                .datetime(postBookmarkDto.getDatetime())
                .url(postBookmarkDto.getUrl())
                .schedule(schedule)
                .build();

        bookmarkRepository.save(bookmark);
    }

    @Transactional(readOnly = true)
    public GetBookmarkListDto getBookmark(Long scheduleId){
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksBySchedule_Id(scheduleId);

        List<GetBookmarkDto> bookmarkDtos = bookmarks.stream()
                .map(bookmark -> GetBookmarkDto.builder()
                        .id(bookmark.getId())
                        .contents(bookmark.getContents())
                        .title(bookmark.getTitle())
                        .url(bookmark.getUrl())
                        .datetime(bookmark.getDatetime())
                        .build())
                .toList();

        return new GetBookmarkListDto(bookmarkDtos);
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        bookmarkRepository.findById(bookmarkId)
                .ifPresent(bookmark -> bookmarkRepository.delete(bookmark));
    }
}
