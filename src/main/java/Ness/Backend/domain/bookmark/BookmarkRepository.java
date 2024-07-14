package Ness.Backend.domain.bookmark;

import Ness.Backend.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findBookmarksBySchedule_Id(Long scheduleId);
}
