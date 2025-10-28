package com.gabrielyorlando.locacao.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteRequestDto;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteResponseDto;
import com.gabrielyorlando.locacao.services.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
class ClienteControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private ClienteService clienteService;

	@Test
	void givenValidRequest_WhenCreate_thenReturn201() throws Exception {
		var request = new ClienteRequestDto();
		request.setNome("Gaby");
		request.setCpf("51982794801");
		request.setTelefone("14981499375");
		request.setEmail("teste1234@gmail.com");

		var response = new ClienteResponseDto();
		response.setId(1L);

		when(clienteService.save(any(ClienteRequestDto.class))).thenReturn(response);

		mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
		                                 .content(objectMapper.writeValueAsString(request)))
		       .andExpect(status().isCreated())
		       .andExpect(jsonPath("$.id").exists());

	}
}