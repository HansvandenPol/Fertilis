package nl.fertilis.fertilis.tasks;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("task")
@Validated
@Slf4j
public class TaskController {

  private final TaskRepository taskRepository;
  private final TaskDaoService taskDaoService;

  public TaskController(TaskRepository taskRepository, TaskDaoService taskDaoService) {
    this.taskRepository = taskRepository;
    this.taskDaoService = taskDaoService;
  }

  @GetMapping("all")
  public ResponseEntity<List<TaskViewModel>> getAllTasksForPeriod() {
    final List<Task> taskList = taskDaoService.getCurrentTasks();
    final List<TaskViewModel> taskViewModels = new ArrayList<>();

    log.info(LocalDate.now().toString());

    taskList.forEach(i -> {
      taskViewModels.add(new TaskViewModel(i.getName(), i.getDescription()));
    });

    log.info("count: {}", taskViewModels.size());


    return ResponseEntity.ok(taskViewModels);
  }

  @GetMapping("period")
  public ResponseEntity<List<TaskViewModel>> getTaskForPeriod(@RequestBody RetrieveTaskViewModel viewModel) {
    final List<Task> taskList = taskDaoService.getTaskByPeriod(viewModel.getPeriod());
    final List<TaskViewModel> taskViewModels = new ArrayList<>();

    taskList.forEach(i -> {
      taskViewModels.add(new TaskViewModel(i.getName(), i.getDescription()));
    });

    return ResponseEntity.ok(taskViewModels);
  }

  @PostMapping
  public ResponseEntity<String> addTask(@RequestBody @Valid TaskRequestData requestData) {
    final Task newTask = new Task(requestData.getName(),requestData.getDescription(), requestData.getPeriod());
    final Task savedTask = taskRepository.save(newTask);
    return ResponseEntity.ok(savedTask.getUuid().toString());
  }
}
