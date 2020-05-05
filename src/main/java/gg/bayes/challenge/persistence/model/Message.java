package gg.bayes.challenge.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@NoArgsConstructor
// TODO: Find an alternative to the not working unique constraint. Maybe composite of
// "matchId", "durationInMillis", "heroName" and a hash value of the whole object?
//@Table(
//    uniqueConstraints = @UniqueConstraint(
//        columnNames = {"matchId", "durationInMillis", "heroName"}
//        )
//)
public abstract class Message {

  @Id
  @GeneratedValue
  private long id;

  @NotNull
  private long matchId;

  @NotNull
  private long durationInMillis;

  @NotNull
  private String heroName;

  public Message(long matchId, long durationInMillis, String heroName) {
    this.matchId = matchId;
    this.durationInMillis = durationInMillis;
    this.heroName = heroName;
  }
}
