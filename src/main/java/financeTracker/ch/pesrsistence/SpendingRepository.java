package financeTracker.ch.pesrsistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpendingRepository extends CrudRepository<Spending, Integer> {
    @Query(value = "SELECT s FROM Spending s WHERE s.creator.id = :user_id")
    List<Spending> findByUserId(@Param("user_id") int userId);
}
