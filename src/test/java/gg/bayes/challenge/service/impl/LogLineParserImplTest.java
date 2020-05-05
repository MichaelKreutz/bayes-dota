package gg.bayes.challenge.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import gg.bayes.challenge.persistence.model.BuyItemMessage;
import gg.bayes.challenge.persistence.model.SpellMessage;
import gg.bayes.challenge.persistence.model.DamageMessage;
import gg.bayes.challenge.persistence.model.KillMessage;
import gg.bayes.challenge.persistence.model.Message;
import gg.bayes.challenge.service.LogLineParser;
import org.junit.jupiter.api.Test;

class LogLineParserImplTest {

  private final LogLineParser logLineParser = new LogLineParserImpl();

  private final long matchId = 1L;

  @Test
  void parseCastSpell() {
    String logLine = "00:08:43.460] npc_dota_hero_pangolier casts ability pangolier_swashbuckle (lvl 1) on dota_unknown";

    final Message message = logLineParser.parse(matchId, logLine).get();

    assertThat(message.getDurationInMillis()).isEqualTo(523460L);
    assertThat(message.getHeroName()).isEqualTo("pangolier");
    assertThat(message).isInstanceOf(SpellMessage.class);
    SpellMessage spellMessage = (SpellMessage) message;
    assertThat(spellMessage.getSpellName()).isEqualTo("pangolier_swashbuckle");
    assertThat(spellMessage.getSpellLevel()).isEqualTo(1);
  }

  @Test
  void parseDamage() {
    final String logLine = "00:10:42.031] npc_dota_hero_bane hits npc_dota_hero_abyssal_underlord with dota_unknown for 51 damage (740->689)";

    final Message message = logLineParser.parse(matchId, logLine).get();

    assertThat(message).isInstanceOf(DamageMessage.class);
    final DamageMessage damageMessage = (DamageMessage) message;
    assertThat(damageMessage.getDamagedHeroName()).isEqualTo("abyssal_underlord");
    assertThat(damageMessage.getDamageType()).isEqualTo("dota_unknown");
    assertThat(damageMessage.getDamageAmount()).isEqualTo(51);
  }


  @Test
  void parseBuyItem() {
    final String logLine = "00:08:46.759] npc_dota_hero_dragon_knight buys item item_quelling_blade";

    final Message message = logLineParser.parse(matchId, logLine).get();

    assertThat(message).isInstanceOf(BuyItemMessage.class);
    final BuyItemMessage buyItemMessage = (BuyItemMessage) message;
    assertThat(buyItemMessage.getItem()).isEqualTo("quelling_blade");
  }

  @Test
  void parseKill() {
    final String logLine = "00:37:05.994] npc_dota_hero_snapfire is killed by npc_dota_hero_bloodseeker";

    final Message message = logLineParser.parse(matchId, logLine).get();
    assertThat(message.getHeroName()).isEqualTo("snapfire");
    assertThat(message).isInstanceOf(KillMessage.class);
    final KillMessage killMessage = (KillMessage) message;
    assertThat(killMessage.getKiller()).isEqualTo("bloodseeker");

  }
}
