package nl.fertilis.fertilis.tasks;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.fertilis.fertilis.period.DateToPeriodConverter;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskDaoService {
  private final TaskRepository taskRepository;

  public TaskDaoService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public List<Task> getCurrentTasks() {
    final String period = DateToPeriodConverter.convert(LocalDate.now());
    log.info("period: {}", period);
    return getTasks(period);
  }

  public List<Task> getTaskByPeriod(LocalDate localDate) {
    final String period = DateToPeriodConverter.convert(localDate);
    return getTasks(period);
  }

  private List<Task> getTasks(String period) {

    return taskRepository.getAllByPeriod(period);
  }
}
