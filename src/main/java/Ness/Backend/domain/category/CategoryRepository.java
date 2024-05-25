package Ness.Backend.domain.category;

import Ness.Backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryById(Long id);

    List<Category> findCategoriesByMember_id(Long memberId);

    List<Category> findCategoriesByName(String name);
}
