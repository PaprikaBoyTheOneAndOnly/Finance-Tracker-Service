package financeTracker.ch.pesrsistence;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SpendingRepository extends CrudRepository<Spending, Integer> {
    @Query(value = "SELECT s FROM Spending s WHERE s.creator.id = :user_id")
    List<Spending> findByUserId(@Param("user_id") int userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Spending s WHERE s.id = :id")
    int deleteSpendingById(@Param("id") int id);

    @Modifying
    @Query(value = "UPDATE Spending s SET s = :spending where s.id = :#{spending.id}")
    int updateSpending(@Param("spending") Spending spending);
}
