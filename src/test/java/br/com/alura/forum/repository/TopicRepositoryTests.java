package br.com.alura.forum.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.alura.forum.model.OpenTopicsByCategory;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.setup.TopicRepositoryTestSetup;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TopicRepositoryTests {

	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Test
	public void shouldSaveATopic() {
		Topic topic = new Topic("Descrição do tópico", "Conteúdo do tópico", null, null);
		
		Topic persistedTopic = this.topicRepository.save(topic);
		Topic foundTopic = this.testEntityManager.find(Topic.class, persistedTopic.getId());
		
		Assertions.assertThat(foundTopic).isNotNull();
		Assertions.assertThat(foundTopic.getShortDescription())
		.isEqualTo(persistedTopic.getShortDescription());
		
	}
	
	@Test
	public void shouldReturnOpenTopicsByCategory() {
		TopicRepositoryTestSetup testeSetup = new TopicRepositoryTestSetup(testEntityManager);
		testeSetup.openTopicsByCategorySetup();
		
		List<OpenTopicsByCategory> openTopics = this.topicRepository.findOpenTopicsByCategory();
		
		Assertions.assertThat(openTopics).isNotEmpty();
		Assertions.assertThat(openTopics).hasSize(2);
		
		Assertions.assertThat(openTopics.get(0).getCategoryName()).isEqualTo("Programação");
		Assertions.assertThat(openTopics.get(0).getTopicCount()).isEqualTo(2);
		
		Assertions.assertThat(openTopics.get(1).getCategoryName()).isEqualTo("Front-end");
		Assertions.assertThat(openTopics.get(1).getTopicCount()).isEqualTo(1);
	}
	
}
