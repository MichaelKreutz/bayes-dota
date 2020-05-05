package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.Message;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface LogLineParser {

  @NotNull
  Optional<Message> parse(long matchId, String logLine);
}
