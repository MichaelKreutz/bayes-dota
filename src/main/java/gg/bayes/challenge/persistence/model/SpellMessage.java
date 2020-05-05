package gg.bayes.challenge.persistence.model;

import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class SpellMessage extends Message {

  private String spellName;
  private int spellLevel;

  public SpellMessage(
      final long matchId,
      final long durationInMillis,
      final String heroName,
      final String spellName,
      final int spellLevel
  ) {
    super(matchId, durationInMillis, heroName);
    this.spellName = spellName;
    this.spellLevel = spellLevel;
  }

}
