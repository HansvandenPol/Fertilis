package nl.fertilis.fertilis.tasks;

import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends ListCrudRepository<Task, Long> {
  @Query("SELECT * FROM task WHERE :period = ANY(period)")
  List<Task> getAllByPeriod(String period);
}
