package nl.fertilis.fertilis.tasks;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestData {
  @NotNull
  private String name;
  @NotNull
  private String description;
  @NotNull
  private String[] period;
}
