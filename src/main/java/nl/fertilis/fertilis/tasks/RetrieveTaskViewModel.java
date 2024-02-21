package nl.fertilis.fertilis.tasks;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveTaskViewModel {
  private LocalDate period;
}
