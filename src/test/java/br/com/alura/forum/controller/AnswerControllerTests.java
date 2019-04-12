package br.com.alura.forum.controller;

import java.net.URI;

import javax.transaction.Transactional;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.forum.controller.dto.input.NewAnswerInputDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")

public class AnswerControllerTests {

	private static final String ENDPOINT = "api/topics/{topicId}/answers";
	
	private Long topicId;
	private String jwt;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldProcessSuccessfullyNewAnswerRequest() throws Exception {
		URI uri = new UriTemplate(ENDPOINT).expand(this.topicId);
		
		NewAnswerInputDto inputDto = new NewAnswerInputDto();
		inputDto.setContent("Não consigo subir servidor");
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+ this.jwt)
				.content(new ObjectMapper().writeValueAsString(inputDto));
		
		this.mockMvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(inputDto.getContent())));
	}
	
}
