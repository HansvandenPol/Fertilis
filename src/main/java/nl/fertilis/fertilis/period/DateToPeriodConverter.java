package nl.fertilis.fertilis.period;

import java.time.LocalDate;

public class DateToPeriodConverter {
  public static String convert(LocalDate localDate) {
    final int monthValue = localDate.getMonthValue();
    final int dayOfMonth = localDate.getDayOfMonth();

    int firstHalfOfMonth = 0;
    if(dayOfMonth > 15) {
      firstHalfOfMonth = 1;
    }

    final StringBuilder builder = new StringBuilder();
    builder.append(monthValue-1);
    builder.append(" " + firstHalfOfMonth);
    return builder.toString();
  }

}
