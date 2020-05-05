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
public class KillMessage extends Message {

  private String killer;

  public KillMessage(
      final long matchId,
      final long durationInMillis,
      final String heroName,
      final String killer
  ) {
    super(matchId, durationInMillis, heroName);
    this.killer = killer;
  }
}
