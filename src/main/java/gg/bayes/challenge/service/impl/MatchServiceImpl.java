package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.BuyItemMessageRepository;
import gg.bayes.challenge.persistence.DamageMessageRepository;
import gg.bayes.challenge.persistence.KillMessageRepository;
import gg.bayes.challenge.persistence.MessageRepository;
import gg.bayes.challenge.persistence.SpellMessageRepository;
import gg.bayes.challenge.persistence.model.BuyItemMessage;
import gg.bayes.challenge.persistence.model.DamageMessage;
import gg.bayes.challenge.persistence.model.KillMessage;
import gg.bayes.challenge.persistence.model.SpellMessage;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.LogLineParser;
import gg.bayes.challenge.service.MatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

  private static final String MATCH_1_START = "[00:00:04.999]";
  private static final String MATCH_2_START = "[00:00:08.266]";

  private final MessageRepository messageRepository;
  private final KillMessageRepository killMessageRepository;
  private final BuyItemMessageRepository buyItemMessageRepository;
  private final DamageMessageRepository damageMessageRepository;
  private final SpellMessageRepository spellMessageRepository;
  private final LogLineParser logLineParser;


  public MatchServiceImpl(
      final MessageRepository messageRepository,
      final KillMessageRepository killMessageRepository,
      final BuyItemMessageRepository buyItemMessageRepository,
      final DamageMessageRepository damageMessageRepository,
      final SpellMessageRepository spellMessageRepository,
      final LogLineParser logLineParser
  ) {
    this.messageRepository = messageRepository;
    this.killMessageRepository = killMessageRepository;
    this.buyItemMessageRepository = buyItemMessageRepository;
    this.damageMessageRepository = damageMessageRepository;
    this.spellMessageRepository = spellMessageRepository;
    this.logLineParser = logLineParser;
  }

  @Override
  public @NotNull Long ingestMatch(final String payload) {
    final long matchId = determineMatchId(payload);
    final List<String> logLines = Arrays.asList(payload.split("\\["));
    final long numberOfAnalyzedMessages = logLines.stream()
        .filter(logLine -> !logLine.trim().isEmpty())
        .map(logLine -> logLineParser.parse(matchId, logLine))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(messageRepository::save)
        .count();
    log.info("Match {} consists of {} analyzed messages.", matchId, numberOfAnalyzedMessages);

    return matchId;
  }

  @Override
  public @NotNull List<HeroKills> findMatchByMatchId(final Long matchId) {
    final List<KillMessage> killMessages = killMessageRepository.findByMatchId(matchId);
    final Map<String, Long> killerToKillsMap = killMessages.stream()
        .collect(Collectors.groupingBy(
            KillMessage::getKiller,
            Collectors.counting()
        ));

    return killerToKillsMap.entrySet().stream()
        .map(entry -> new HeroKills(entry.getKey(), entry.getValue().intValue()))
        .collect(Collectors.toList());
  }

  @Override
  public @NotNull List<HeroItems> findItemsByMatchIdAndHero(final Long matchId,
      final String heroName) {
    final List<BuyItemMessage> buyItemMessages = buyItemMessageRepository
        .findByMatchIdAndHeroName(matchId, heroName);
    return buyItemMessages.stream()
        .map(message -> new HeroItems(message.getItem(), message.getDurationInMillis()))
        .collect(Collectors.toList());
  }


  @Override
  public @NotNull List<HeroSpells> findSpellsByMatchIdAndHero(final Long matchId,
      final String heroName) {
    final List<SpellMessage> spellMessages = spellMessageRepository
        .findByMatchIdAndHeroName(matchId, heroName);
    final Map<String, Long> spellNameToCastCountMap = spellMessages.stream()
        .collect(Collectors.groupingBy(
            SpellMessage::getSpellName,
            Collectors.counting()
        ));
    return spellNameToCastCountMap.entrySet().stream()
        .map(entry -> new HeroSpells(entry.getKey(), entry.getValue().intValue()))
        .collect(Collectors.toList());
  }

  @Override
  public @NotNull List<HeroDamage> findDamageByMatchIdAndHero(Long matchId, String heroName) {
    final List<DamageMessage> damageMessages = damageMessageRepository
        .findByMatchIdAndHeroName(matchId, heroName);
    final Map<String, HeroDamage> heroNameToHeroDamageMap = damageMessages.stream()
        .map(message -> new HeroDamage(message.getDamagedHeroName(), 1,
            message.getDamageAmount()))
        .collect(Collectors.groupingBy(
            HeroDamage::getTarget,
            Collectors.reducing(
                new HeroDamage(null, 0, 0),
                x -> x,
                (damage1, damage2) -> new HeroDamage(
                    damage2.getTarget(),
                    damage1.getDamageInstances() + damage2.getDamageInstances(),
                    damage1.getTotalDamage() + damage2.getTotalDamage()
                )
            )
        ));
    return new ArrayList<>(heroNameToHeroDamageMap.values());
  }

  private Long determineMatchId(final String payload) {
    if (payload.startsWith(MATCH_1_START)) {
      return 1L;
    } else if (payload.startsWith(MATCH_2_START)) {
      return 2L;
    } else {
      return 0L;
    }
  }

}
