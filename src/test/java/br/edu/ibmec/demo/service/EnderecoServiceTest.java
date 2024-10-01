package br.edu.ibmec.demo.service;

import br.edu.ibmec.demo.dto.EnderecoDTO;
import br.edu.ibmec.demo.model.Endereco;
import br.edu.ibmec.demo.repository.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEndereco_Success() {
        // Dados de exemplo para o teste
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setStreet("Rua Exemplo");
        enderecoDTO.setNumber("123");
        enderecoDTO.setNeighborhood("Bairro Exemplo");
        enderecoDTO.setCity("Cidade Exemplo");
        enderecoDTO.setState("SP");
        enderecoDTO.setZipCode("12345-678");

        Endereco endereco = new Endereco();
        endereco.setStreet(enderecoDTO.getStreet());
        endereco.setNumber(enderecoDTO.getNumber());
        endereco.setNeighborhood(enderecoDTO.getNeighborhood());
        endereco.setCity(enderecoDTO.getCity());
        endereco.setState(enderecoDTO.getState());
        endereco.setZipCode(enderecoDTO.getZipCode());

        // Configurando o comportamento do mock
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        // Executando o método de teste
        EnderecoDTO result = enderecoService.addEndereco(enderecoDTO);

        // Verificando os resultados
        assertNotNull(result);
        assertEquals(enderecoDTO.getStreet(), result.getStreet());
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }
}
