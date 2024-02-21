package nl.fertilis.fertilis.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskViewModel {
  private String name;
  private String description;
}
