package gg.bayes.challenge.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import gg.bayes.challenge.persistence.model.Message;
import gg.bayes.challenge.persistence.model.SpellMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Test
  void createMessage() {
    final SpellMessage message = new SpellMessage();
    message.setMatchId(1L);
    message.setDurationInMillis(500L);
    message.setHeroName("Raistlin Majere");
    message.setSpellLevel(5);
    message.setSpellName("icebolt");

    final SpellMessage expectedMessage = messageRepository.save(message);

    final Message messageFromDb = messageRepository.findById(expectedMessage.getId()).get();
    assertThat(messageFromDb).isEqualTo(expectedMessage);
  }
}
