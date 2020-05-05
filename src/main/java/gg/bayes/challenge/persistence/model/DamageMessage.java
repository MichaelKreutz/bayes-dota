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
public class DamageMessage extends Message {

  private String damagedHeroName;
  private String damageType;
  private int damageAmount;

  public DamageMessage(
      final long matchId,
      final long durationInMillis,
      final String heroName,
      final String damagedHeroName,
      final String damageType,
      final int damageAmount
  ) {
    super(matchId, durationInMillis, heroName);
    this.damagedHeroName = damagedHeroName;
    this.damageType = damageType;
    this.damageAmount = damageAmount;
  }
}
