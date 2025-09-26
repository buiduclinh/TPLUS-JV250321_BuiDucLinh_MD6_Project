package ra.edu.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query("""
                SELECT DISTINCT u
                FROM User u
                JOIN u.role r
                WHERE (:role IS NULL OR r.role = :role)
                  AND (:status IS NULL OR u.isActive = :status)
            """)
    Page<User> findAll(Pageable pageable,
                       @Param("role") String role,
                       @Param("status") Boolean status);
    @Query("SELECT u FROM User u WHERE u.isActive = :status")
    Page<User> findAllUserByStatus(@Param("status") Boolean status, Pageable pageable);

}
