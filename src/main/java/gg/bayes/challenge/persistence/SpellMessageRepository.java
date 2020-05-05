package gg.bayes.challenge.persistence;

import gg.bayes.challenge.persistence.model.SpellMessage;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpellMessageRepository extends JpaRepository<SpellMessage, Long> {

  @NotNull
  List<SpellMessage> findByMatchIdAndHeroName(Long matchId, String heroName);
}
