package nl.fertilis.fertilis.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public enum Tasks {
  PLANT_TREE("Als het niet vriest en de grond niet bevroren is, kun je planten, heesters, bomen en hagen planten", 0),
  EXPAND_SNOWDROP("Wilt u meer sneeuwklokjes in de tuin rooi dan vlak na de bloei enkele pollen voorzichtig op, trek de bolletjes van elkaar los en plant ze opnieuw in kleine groepjes.", 0);

  private final String task;
  private final int month;
}
