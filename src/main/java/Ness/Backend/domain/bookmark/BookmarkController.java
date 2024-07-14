package Ness.Backend.domain.bookmark;

import Ness.Backend.domain.bookmark.dto.request.PostBookmarkDto;
import Ness.Backend.domain.bookmark.dto.response.GetBookmarkListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("")
    public ResponseEntity<?> createBookmark(@RequestBody PostBookmarkDto postBookmarkDto){
        bookmarkService.createBookmark(postBookmarkDto);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @GetMapping("")
    public ResponseEntity<GetBookmarkListDto> getBookmark(@RequestParam Long scheduleId){
        GetBookmarkListDto listDto = bookmarkService.getBookmark(scheduleId);
        return new ResponseEntity<>(listDto, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteBookmark(@RequestParam Long bookmarkId){
        bookmarkService.deleteBookmark(bookmarkId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
