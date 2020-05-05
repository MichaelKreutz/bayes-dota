package gg.bayes.challenge.persistence;

import gg.bayes.challenge.persistence.model.KillMessage;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KillMessageRepository extends JpaRepository<KillMessage, Long> {

  @NotNull
  List<KillMessage> findByMatchId(Long matchId);
}
