package gg.bayes.challenge.service;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface MatchService {

  @NotNull
  Long ingestMatch(String payload);

  @NotNull
  List<HeroKills> findMatchByMatchId(Long matchId);

  @NotNull
  List<HeroItems> findItemsByMatchIdAndHero(Long matchId, String heroName);

  @NotNull
  List<HeroSpells> findSpellsByMatchIdAndHero(Long matchId, String heroName);

  @NotNull
  List<HeroDamage> findDamageByMatchIdAndHero(Long matchId, String heroName);
}
