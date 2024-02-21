package nl.fertilis.fertilis.tasks;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Task {
  @Id
  private Long id;
  private UUID uuid = UUID.randomUUID();

  @NonNull
  private String name;

  @NonNull
  private String description;
  private String category = "placeholder"; // For now

  @NonNull
  private String[] period;
}
