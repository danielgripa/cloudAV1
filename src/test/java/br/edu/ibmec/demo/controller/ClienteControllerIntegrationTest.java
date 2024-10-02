package br.edu.ibmec.demo.controller;

import br.edu.ibmec.demo.dto.ClienteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerIntegrationTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        // Configuração manual do MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Inicialização manual do ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testAddCliente_InvalidPhone() throws Exception {
        // Criação de um ClienteDTO com telefone inválido
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setName("John Doe");
        clienteDTO.setEmail("john.doe@example.com");
        clienteDTO.setCpf("123.456.789-00");
        clienteDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        clienteDTO.setPhone("12345678"); // Telefone fora do padrão

        // Executar a requisição POST para adicionar cliente
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(clienteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.phone").value("Telefone deve seguir o padrão (XX) XXXXX-XXXX"));
    }

    @Test
    public void testAddCliente_InvalidEmail() throws Exception {
        // Criação de um ClienteDTO com email inválido
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setName("John Doe");
        clienteDTO.setEmail("invalid-email"); // Email fora do padrão
        clienteDTO.setCpf("123.456.789-00");
        clienteDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        clienteDTO.setPhone("(11) 99999-9999");

        // Executar a requisição POST para adicionar cliente
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(clienteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("Email deve ser válido (ex.: usuario@dominio.com)"));
    }

    @Test
    public void testAddCliente_InvalidCPF() throws Exception {
        // Criação de um ClienteDTO com CPF inválido
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setName("John Doe");
        clienteDTO.setEmail("john.doe@example.com");
        clienteDTO.setCpf("12345678900"); // CPF fora do padrão
        clienteDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        clienteDTO.setPhone("(11) 99999-9999");

        // Executar a requisição POST para adicionar cliente
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(clienteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.cpf").value("O CPF deve seguir o padrão XXX.XXX.XXX-XX"));
    }
    @Test
    public void testAddExistingEnderecoToCliente_ClienteNotFound() throws Exception {
        // IDs fictícios para teste - cliente inexistente
        Long clienteId = 999L;
        Long enderecoId = 1L;

        // Executar a requisição POST para associar endereço ao cliente inexistente
        mockMvc.perform(post("/api/clientes/" + clienteId + "/enderecos/" + enderecoId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente não encontrado com o ID: " + clienteId));
    }

    @Test
    public void testAddExistingEnderecoToCliente_EnderecoNotFound() throws Exception {
        // IDs fictícios para teste - endereço inexistente
        Long clienteId = 1L;
        Long enderecoId = 999L;

        // Executar a requisição POST para associar endereço inexistente ao cliente
        mockMvc.perform(post("/api/clientes/" + clienteId + "/enderecos/" + enderecoId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Endereço não encontrado com o ID: " + enderecoId));
    }

    @Test
    public void testRemoveEnderecoFromCliente_ClienteNotFound() throws Exception {
        // IDs fictícios para teste - cliente inexistente
        Long clienteId = 999L;
        Long enderecoId = 1L;

        // Executar a requisição DELETE para desassociar endereço do cliente inexistente
        mockMvc.perform(delete("/api/clientes/" + clienteId + "/enderecos/" + enderecoId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente não encontrado com o ID: " + clienteId));
    }

    @Test
    public void testRemoveEnderecoFromCliente_EnderecoNotFound() throws Exception {
        // IDs fictícios para teste - endereço inexistente
        Long clienteId = 1L;
        Long enderecoId = 999L;

        // Executar a requisição DELETE para desassociar endereço inexistente do cliente
        mockMvc.perform(delete("/api/clientes/" + clienteId + "/enderecos/" + enderecoId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Endereço não encontrado com o ID: " + enderecoId));
    }



    // Função auxiliar para converter o objeto para JSON
    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
