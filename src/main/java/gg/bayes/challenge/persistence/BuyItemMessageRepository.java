package gg.bayes.challenge.persistence;

import gg.bayes.challenge.persistence.model.BuyItemMessage;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyItemMessageRepository extends JpaRepository<BuyItemMessage, Long> {

  @NotNull
  List<BuyItemMessage> findByMatchIdAndHeroName(Long matchId, String heroName);

}
