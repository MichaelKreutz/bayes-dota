package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.model.BuyItemMessage;
import gg.bayes.challenge.persistence.model.DamageMessage;
import gg.bayes.challenge.persistence.model.KillMessage;
import gg.bayes.challenge.persistence.model.Message;
import gg.bayes.challenge.persistence.model.SpellMessage;
import gg.bayes.challenge.service.LogLineParser;
import gg.bayes.challenge.utils.Utils;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogLineParserImpl implements LogLineParser {

  private static final Pattern TIME_HERO_PATTERN = Pattern
      .compile("(.+)] npc_dota_hero_([a-zA-Z_]+)(?:'s)? (.*)");
  private static final Pattern BUY_ITEM_PATTERN = Pattern.compile(".*buys item item_([a-zA-Z_]+)");
  private static final Pattern CAST_SPELL_PATTERN = Pattern
      .compile(".*casts ability ([a-zA-Z_]+) \\(lvl ([\\d]+)\\).*");
  private static final Pattern DAMAGE_PATTERN = Pattern
      .compile(".*hits npc_dota_hero_([a-zA-Z_]+) with ([a-zA-Z_]+) for ([\\d]+) damage.*");
  private static final Pattern KILL_PATTERN = Pattern
      .compile(".*is killed by npc_dota_hero_([a-zA-Z_]+).*");

  @Override
  public @NotNull Optional<Message> parse(final long matchId, final String logLine) {
    final Matcher matcher = TIME_HERO_PATTERN.matcher(logLine);
    if (matcher.matches()) {
      final String timeString = matcher.group(1);
      long durationInMillis = Utils.calcDurationInMsFromTime(timeString);
      final String heroName = matcher.group(2);
      final String subMessage = matcher.group(3);
      return parseSubMessage(matchId, durationInMillis, heroName, subMessage);
    }
    return Optional.empty();
  }

  private Optional<Message> parseSubMessage(
      final long matchId,
      final long durationInMillis,
      final String heroName,
      final String subMessage
  ) {
    final Matcher buyItemMatcher = BUY_ITEM_PATTERN.matcher(subMessage);
    final Matcher castSpellMatcher = CAST_SPELL_PATTERN.matcher(subMessage);
    final Matcher damageMatcher = DAMAGE_PATTERN.matcher(subMessage);
    final Matcher killMatcher = KILL_PATTERN.matcher(subMessage);

    if (buyItemMatcher.matches()) {
      return Optional.of(
          new BuyItemMessage(matchId, durationInMillis, heroName,
              buyItemMatcher.group(1))
      );
    } else if (castSpellMatcher.matches()) {
      return Optional.of(
          new SpellMessage(matchId, durationInMillis, heroName, castSpellMatcher.group(1),
              Integer.parseInt(castSpellMatcher.group(2)))
      );
    } else if (damageMatcher.matches()) {
      return Optional.of(
          new DamageMessage(matchId, durationInMillis, heroName, damageMatcher.group(1),
              damageMatcher.group(2), Integer.parseInt(damageMatcher.group(3)))
      );
    } else if (killMatcher.matches()) {
      return Optional.of(
          new KillMessage(matchId, durationInMillis, heroName, killMatcher.group(1))
      );
    } else {
      return Optional.empty();
    }
  }

}
