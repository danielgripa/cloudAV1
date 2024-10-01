package br.edu.ibmec.demo.controller;

import br.edu.ibmec.demo.dto.EnderecoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EnderecoControllerIntegrationTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Long enderecoId;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setStreet("Rua Test");
        enderecoDTO.setNumber("123");
        enderecoDTO.setNeighborhood("Bairro Test");
        enderecoDTO.setCity("Cidade Test");
        enderecoDTO.setState("SP");
        enderecoDTO.setZipCode("12345-678");

        try {
            String response = mockMvc.perform(post("/api/enderecos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(enderecoDTO)))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            System.out.println("Response JSON: " + response);

            // Parse the response to get the ID
            JsonNode jsonNode = objectMapper.readTree(response);
            enderecoId = jsonNode.get("id").asLong();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Test for successful address creation
    @Test
    public void testCreateEndereco_Success() throws Exception {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setStreet("Rua A");
        enderecoDTO.setNumber("123");
        enderecoDTO.setNeighborhood("Bairro B");
        enderecoDTO.setCity("Cidade C");
        enderecoDTO.setState("SP");
        enderecoDTO.setZipCode("12345-678");

        mockMvc.perform(post("/api/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(enderecoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.street").value("Rua A"))
                .andExpect(jsonPath("$.number").value("123"));
    }

    // Test for invalid zip code format
    @Test
    public void testCreateEndereco_InvalidZipCode() throws Exception {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setStreet("Rua A");
        enderecoDTO.setNumber("123");
        enderecoDTO.setNeighborhood("Bairro B");
        enderecoDTO.setCity("Cidade C");
        enderecoDTO.setState("SP");
        enderecoDTO.setZipCode("12345678"); // Invalid format

        mockMvc.perform(post("/api/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(enderecoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.zipCode").value("CEP deve seguir o padrão XXXXX-XXX"));
    }

    // Test for successful retrieval of all addresses
    @Test
    public void testGetAllEnderecos_Success() throws Exception {
        mockMvc.perform(get("/api/enderecos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }

    // Test for retrieving address by ID (Success case)
    @Test
    public void testGetEnderecoById_Success() throws Exception {
        // Assume an address with ID 1 exists
        mockMvc.perform(get("/api/enderecos/" + enderecoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(enderecoId));
    }

    // Test for retrieving address by ID that doesn't exist
    @Test
    public void testGetEnderecoById_NotFound() throws Exception {
        mockMvc.perform(get("/api/enderecos/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Endereço com ID 9999 não encontrado"));
    }

    // Test for successful address update
    @Test    public void testUpdateEndereco_Success() throws Exception {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setStreet("Rua B");
        enderecoDTO.setNumber("321");
        enderecoDTO.setNeighborhood("Bairro Z");
        enderecoDTO.setCity("Cidade Y");
        enderecoDTO.setState("RJ");
        enderecoDTO.setZipCode("87654-321");

        // Assume an address with ID 1 exists
        mockMvc.perform(put("/api/enderecos/" + enderecoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(enderecoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Rua B"))
                .andExpect(jsonPath("$.number").value("321"));
    }

    // Test for updating address with invalid zip code
    @Test
    public void testUpdateEndereco_InvalidZipCode() throws Exception {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setStreet("Rua C");
        enderecoDTO.setNumber("456");
        enderecoDTO.setNeighborhood("Bairro X");
        enderecoDTO.setCity("Cidade W");
        enderecoDTO.setState("MG");
        enderecoDTO.setZipCode("12345678"); // Invalid format

        // Assume an address with ID 1 exists
        mockMvc.perform(put("/api/enderecos/" + enderecoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(enderecoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.zipCode").value("CEP deve seguir o padrão XXXXX-XXX"));
    }

    // Test for deleting an address successfully
    @Test
    public void testDeleteEndereco_Success() throws Exception {
        // Assume an address with ID 1 exists
        mockMvc.perform(delete("/api/enderecos/" + enderecoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // Test for deleting a non-existing address
    @Test
    public void testDeleteEndereco_NotFound() throws Exception {
        mockMvc.perform(delete("/api/enderecos/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Endereço com ID 9999 não encontrado"));
    }

    // Utility function for JSON conversion
    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
