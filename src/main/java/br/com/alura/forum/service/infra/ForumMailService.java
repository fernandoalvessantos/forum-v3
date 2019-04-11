package br.com.alura.forum.service.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.alura.forum.infra.NewReplyMailFactory;
import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.topic.domain.Topic;

@Service
public class ForumMailService {

	private static final Logger logger = LoggerFactory.getLogger(ForumMailService.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private NewReplyMailFactory newReplyMailFactory;
	
	@Async
	public void sendNewReplyMail(Answer answer) {
		Topic answeredTopic = answer.getTopic();
		
		MimeMessagePreparator messagePreparator = (mimeMessage) -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage); 
			
			messageHelper.setTo(answeredTopic.getOwnerEmail());
			messageHelper.setSubject("Novo comentário em: "+answeredTopic.getShortDescription());
			
			String messageContent = this.newReplyMailFactory.generateNewReplyMailContent(answer);
			messageHelper.setText(messageContent, true);
		};
		
		try {
			mailSender.send(messagePreparator);
		} catch (MailException e) {
			Topic answeredTopic2 = answer.getTopic();
			logger.error("Não foi possível notificar o usuário {} enviando email para {}",
					answeredTopic2.getOwnerName(), answeredTopic2.getOwnerEmail());
		}
	}
}
