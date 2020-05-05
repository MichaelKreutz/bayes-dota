package gg.bayes.challenge.utils;


import java.time.Duration;

public final class Utils {

  private Utils() {
  }

  public static long calcDurationInMsFromTime(final String timeString) {
    String durationString = timeString
        .replaceFirst("(\\d{2}):(\\d{2}):(\\d{2}\\.\\d{3})", "PT$1H$2M$3S");
    final Duration duration = Duration.parse(durationString);

    return duration.toMillis();
  }
}
