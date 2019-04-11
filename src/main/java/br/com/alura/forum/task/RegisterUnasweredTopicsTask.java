package br.com.alura.forum.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alura.forum.model.OpenTopicsByCategory;
import br.com.alura.forum.repository.OpenTopicsByCategoryRepository;
import br.com.alura.forum.repository.TopicRepository;

@Component
public class RegisterUnasweredTopicsTask {

	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private OpenTopicsByCategoryRepository openTopicsByCategoryRepository;
	
	public void execute() {
		List<OpenTopicsByCategory> topics = topicRepository.findOpenTopicsByCategory();
		
		this.openTopicsByCategoryRepository.salveAll(topics);
	}
}
