package gg.bayes.challenge.persistence;

import gg.bayes.challenge.persistence.model.DamageMessage;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DamageMessageRepository extends JpaRepository<DamageMessage, Long> {

  @NotNull
  List<DamageMessage> findByMatchIdAndHeroName(Long matchId, String heroName);
}
