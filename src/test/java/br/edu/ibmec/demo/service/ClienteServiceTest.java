package br.edu.ibmec.demo.service;

import br.edu.ibmec.demo.dto.ClienteDTO;
import br.edu.ibmec.demo.exception.BadRequestException;
import br.edu.ibmec.demo.exception.ResourceNotFoundException;
import br.edu.ibmec.demo.model.Cliente;
import br.edu.ibmec.demo.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCliente_Success() {
        // Dados de exemplo para o teste
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setName("John Doe");
        clienteDTO.setEmail("johndoe@example.com");
        clienteDTO.setCpf("123.456.789-00");
        clienteDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        clienteDTO.setPhone("(11) 99999-9999");

        Cliente cliente = new Cliente();
        cliente.setName(clienteDTO.getName());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setBirthDate(clienteDTO.getBirthDate());
        cliente.setPhone(clienteDTO.getPhone());

        // Configurando o comportamento do mock
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Executando o método de teste
        ClienteDTO result = clienteService.addCliente(clienteDTO);

        // Verificando os resultados
        assertNotNull(result);
        assertEquals(clienteDTO.getName(), result.getName());
        assertEquals(clienteDTO.getEmail(), result.getEmail());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testAddCliente_EmailAlreadyExists() {
        // Dados de exemplo para o teste
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setName("John Doe");
        clienteDTO.setEmail("johndoe@example.com");
        clienteDTO.setCpf("123.456.789-00");
        clienteDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        clienteDTO.setPhone("(11) 99999-9999");

        // Simula o comportamento do repositório quando o email já existe
        when(clienteRepository.findByEmail(clienteDTO.getEmail())).thenReturn(Optional.of(new Cliente()));

        // Verifica se a exceção é lançada corretamente
        Exception exception = assertThrows(BadRequestException.class, () -> {
            clienteService.addCliente(clienteDTO);
        });

        assertEquals("O email fornecido já está em uso.", exception.getMessage());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    public void testAddCliente_CpfAlreadyExists() {
        // Dados de exemplo para o teste
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setName("John Doe");
        clienteDTO.setEmail("johndoe@example.com");
        clienteDTO.setCpf("123.456.789-00");
        clienteDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        clienteDTO.setPhone("(11) 99999-9999");

        // Simula o comportamento do repositório quando o CPF já existe
        when(clienteRepository.findByCpf(clienteDTO.getCpf())).thenReturn(Optional.of(new Cliente()));

        // Verifica se a exceção é lançada corretamente
        Exception exception = assertThrows(BadRequestException.class, () -> {
            clienteService.addCliente(clienteDTO);
        });

        assertEquals("O CPF fornecido já está em uso.", exception.getMessage());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    public void testGetClienteById_Success() {
        // Dados de exemplo para o teste
        Long clienteId = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setName("John Doe");
        cliente.setEmail("johndoe@example.com");
        cliente.setCpf("123.456.789-00");

        // Configurar o mock para retornar o cliente ao buscar por ID
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        // Executar o método de teste
        ClienteDTO result = clienteService.getClienteById(clienteId);

        // Verificar os resultados
        assertNotNull(result);
        assertEquals(cliente.getEmail(), result.getEmail());
        assertEquals(cliente.getCpf(), result.getCpf());
        verify(clienteRepository, times(1)).findById(clienteId);
    }

    @Test
    public void testGetClienteById_NotFound() {
        // Simula o comportamento do repositório quando o ID não é encontrado
        Long clienteId = 1L;
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        // Verifica se a exceção é lançada corretamente
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.getClienteById(clienteId);
        });

        assertEquals("Cliente com ID " + clienteId + " não encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById(clienteId);
    }

    @Test
    public void testGetAllClientes_Success() {
        // Dados de exemplo para o teste
        List<Cliente> clientes = new ArrayList<>();
        Cliente cliente1 = new Cliente();
        cliente1.setName("John Doe");

        Cliente cliente2 = new Cliente();
        cliente2.setName("Jane Smith");

        clientes.add(cliente1);
        clientes.add(cliente2);

        // Configurar o mock para retornar uma lista de clientes
        when(clienteRepository.findAll()).thenReturn(clientes);

        // Executar o método de teste
        List<ClienteDTO> result = clienteService.getAllClientes();

        // Verificar os resultados
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateCliente_Success() {
        // Dados de exemplo para o teste
        Long clienteId = 1L;
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setName("Updated Name");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(clienteId);
        clienteExistente.setName("Old Name");

        // Configurar o mock para encontrar o cliente e atualizá-lo
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);

        // Executar o método de teste
        ClienteDTO result = clienteService.updateCliente(clienteId, clienteDTO);

        // Verificar os resultados
        assertNotNull(result);
        assertEquals(clienteDTO.getName(), result.getName());
        verify(clienteRepository, times(1)).findById(clienteId);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testDeleteCliente_Success() {
        // Dados de exemplo para o teste
        Long clienteId = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        // Configurar o mock para encontrar o cliente
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        // Executar o método de teste
        clienteService.deleteCliente(clienteId);

        // Verificar os resultados
        verify(clienteRepository, times(1)).findById(clienteId);
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    public void testDeleteCliente_NotFound() {
        // Simula o comportamento do repositório quando o ID não é encontrado
        Long clienteId = 1L;
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        // Verifica se a exceção é lançada corretamente
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.deleteCliente(clienteId);
        });

        assertEquals("Cliente com ID " + clienteId + " não encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById(clienteId);
    }

    @Test
    public void testAddCliente_AgeValidation() {
        // Dados de exemplo para o teste com idade menor que 18 anos
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setName("Young Client");
        clienteDTO.setEmail("youngclient@example.com");
        clienteDTO.setCpf("123.456.789-00");
        clienteDTO.setBirthDate(LocalDate.now().minusYears(17)); // Idade menor que 18 anos
        clienteDTO.setPhone("(11) 99999-9999");

        // Verifica se a exceção é lançada corretamente ao tentar adicionar cliente com idade menor que 18
        Exception exception = assertThrows(BadRequestException.class, () -> {
            clienteService.addCliente(clienteDTO);
        });

        assertEquals("O cliente deve ter pelo menos 18 anos.", exception.getMessage());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
}
