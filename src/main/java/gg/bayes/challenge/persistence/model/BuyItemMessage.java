package gg.bayes.challenge.persistence.model;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class BuyItemMessage extends Message {

  private String item;

  public BuyItemMessage(
      final long matchId,
      final long durationInMillis,
      final String heroName,
      final String item
  ) {
    super(matchId, durationInMillis, heroName);
    this.item = item;
  }
}
