package ra.edu.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.model.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findAllByCourse_CourseId(Pageable pageable, Long courseId);
}
