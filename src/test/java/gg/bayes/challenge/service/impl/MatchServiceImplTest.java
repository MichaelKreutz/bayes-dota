package gg.bayes.challenge.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import gg.bayes.challenge.persistence.model.BuyItemMessage;
import gg.bayes.challenge.persistence.model.DamageMessage;
import gg.bayes.challenge.persistence.model.SpellMessage;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroSpells;
import java.util.List;
import gg.bayes.challenge.persistence.BuyItemMessageRepository;
import gg.bayes.challenge.persistence.SpellMessageRepository;
import gg.bayes.challenge.persistence.DamageMessageRepository;
import gg.bayes.challenge.persistence.KillMessageRepository;
import gg.bayes.challenge.persistence.MessageRepository;
import gg.bayes.challenge.persistence.model.KillMessage;
import gg.bayes.challenge.rest.model.HeroKills;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchServiceImplTest {

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private KillMessageRepository killMessageRepository;
  @Mock
  private DamageMessageRepository damageMessageRepository;
  @Mock
  private BuyItemMessageRepository buyItemMessageRepository;
  @Mock
  private SpellMessageRepository spellMessageRepository;

  @InjectMocks
  private MatchServiceImpl matchService;


  @Test
  void findMatchByMatchId() {
    final long matchId = 1L;
    final String caramon = "caramon";
    final String raist = "raist";
    when(killMessageRepository.findByMatchId(matchId)).thenReturn(Arrays.asList(
        createKillMessage(caramon),
        createKillMessage(caramon),
        createKillMessage(caramon),
        createKillMessage(raist),
        createKillMessage(raist)
    ));

    final List<HeroKills> heroKills = matchService.findMatchByMatchId(matchId);
    assertThat(heroKills).hasSize(2);
    final HeroKills heroKills1 = heroKills.get(0);
    assertThat(heroKills1.getHero()).isEqualTo(caramon);
    assertThat(heroKills1.getKills()).isEqualTo(3);
    final HeroKills heroKills2 = heroKills.get(1);
    assertThat(heroKills2.getHero()).isEqualTo(raist);
    assertThat(heroKills2.getKills()).isEqualTo(2);
  }

  @Test
  void findItemsByMatchIdAndHero() {
    final long matchId = 1L;
    final String heroName = "caramon";
    final String sword = "molten_sword";
    final String axe = "dump_axe";
    when(buyItemMessageRepository.findByMatchIdAndHeroName(matchId, heroName))
        .thenReturn(Arrays.asList(
            createItemBuyMessage(sword, 5L),
            createItemBuyMessage(axe, 7L)
        ));

    final List<HeroItems> heroItems = matchService
        .findItemsByMatchIdAndHero(matchId, heroName);

    assertThat(heroItems).hasSize(2);
    final HeroItems heroItems1 = heroItems.get(0);
    assertThat(heroItems1.getItem()).isEqualTo(sword);
    assertThat(heroItems1.getTimestamp()).isEqualTo(5L);
    final HeroItems heroItems2 = heroItems.get(1);
    assertThat(heroItems2.getItem()).isEqualTo(axe);
    assertThat(heroItems2.getTimestamp()).isEqualTo(7L);
  }

  @Test
  void findSpellsByMatchIdAndHero() {
    final long matchId = 1L;
    final String heroName = "raistlin";
    final String abracadabra = "abracadabra";
    final String hexHex = "hex hex...";
    when(spellMessageRepository.findByMatchIdAndHeroName(matchId, heroName))
        .thenReturn(Arrays.asList(
            createSpellMessage(abracadabra),
            createSpellMessage(hexHex),
            createSpellMessage(hexHex),
            createSpellMessage(abracadabra),
            createSpellMessage(hexHex)
        ));

    final List<HeroSpells> heroSpells = matchService
        .findSpellsByMatchIdAndHero(matchId, heroName);

    assertThat(heroSpells).hasSize(2);
    final HeroSpells heroSpells2 = heroSpells.get(0);
    assertThat(heroSpells2.getSpell()).isEqualTo(hexHex);
    assertThat(heroSpells2.getCasts()).isEqualTo(3);
    final HeroSpells heroSpells1 = heroSpells.get(1);
    assertThat(heroSpells1.getSpell()).isEqualTo(abracadabra);
    assertThat(heroSpells1.getCasts()).isEqualTo(2);
  }

  @Test
  void findDamageByMatchIdAndHero() {
    final long matchId = 1L;
    final String heroName = "caramon";
    final String ogre = "dump_ogre";
    final String goblin = "swift_goblin";
    when(damageMessageRepository.findByMatchIdAndHeroName(matchId, heroName))
        .thenReturn(Arrays.asList(
            createDamageMessage(ogre, 5),
            createDamageMessage(goblin, 7),
            createDamageMessage(ogre, 15)
        ));

    final List<HeroDamage> heroDamages = matchService
        .findDamageByMatchIdAndHero(matchId, heroName);

    assertThat(heroDamages).hasSize(2);
    final HeroDamage goblinDamages = heroDamages.get(0);
    assertThat(goblinDamages.getTarget()).isEqualTo(goblin);
    assertThat(goblinDamages.getDamageInstances()).isEqualTo(1);
    assertThat(goblinDamages.getTotalDamage()).isEqualTo(7);
    final HeroDamage ogreDamage = heroDamages.get(1);
    assertThat(ogreDamage.getTarget()).isEqualTo(ogre);
    assertThat(ogreDamage.getDamageInstances()).isEqualTo(2);
    assertThat(ogreDamage.getTotalDamage()).isEqualTo(20);
  }

  private SpellMessage createSpellMessage(String spellName) {
    final SpellMessage spellMessage = new SpellMessage();
    spellMessage.setSpellName(spellName);
    return spellMessage;
  }

  private DamageMessage createDamageMessage(String target, int damageAmount) {
    final DamageMessage damageMessage = new DamageMessage();
    damageMessage.setDamagedHeroName(target);
    damageMessage.setDamageAmount(damageAmount);
    return damageMessage;
  }

  private KillMessage createKillMessage(final String killer) {
    final KillMessage killMessage = new KillMessage();
    killMessage.setKiller(killer);

    return killMessage;
  }

  private BuyItemMessage createItemBuyMessage(final String item, final long durationInMillis) {
    final BuyItemMessage buyItemMessage = new BuyItemMessage();
    buyItemMessage.setItem(item);
    buyItemMessage.setDurationInMillis(durationInMillis);
    return buyItemMessage;
  }
}
