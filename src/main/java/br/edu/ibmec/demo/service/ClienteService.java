package br.edu.ibmec.demo.service;

import br.edu.ibmec.demo.dto.ClienteDTO;
import br.edu.ibmec.demo.exception.ResourceNotFoundException;
import br.edu.ibmec.demo.model.Cliente;
import br.edu.ibmec.demo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Adicionar um novo cliente
    public ClienteDTO addCliente(ClienteDTO clienteDTO) {

        // Validar idade mínima
        if (!clienteDTO.isAgeValid()) {
            throw new IllegalArgumentException("O cliente deve ter pelo menos 18 anos.");
        }
        // Validação de unicidade do email
        if (clienteRepository.findByEmail(clienteDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("O email fornecido já está em uso.");
        }
        // Validação de unicidade do CPF
        if (clienteRepository.findByCpf(clienteDTO.getCpf()).isPresent()) {
            throw new IllegalArgumentException("O CPF fornecido já está em uso.");
        }

        Cliente cliente = convertToEntity(clienteDTO);
        Cliente novoCliente = clienteRepository.save(cliente);
        return convertToDTO(novoCliente);
    }

    // Atualizar cliente existente
    public ClienteDTO updateCliente(Long id, ClienteDTO clienteDetailsDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id + " não encontrado"));

        // Verificar unicidade do email para outro cliente
        clienteRepository.findByEmail(clienteDetailsDTO.getEmail()).ifPresent(cliente -> {
            if (!cliente.getId().equals(id)) {
                throw new IllegalArgumentException("O email fornecido já está em uso por outro cliente.");
            }
        });

        // Verificar unicidade do CPF para outro cliente
        clienteRepository.findByCpf(clienteDetailsDTO.getCpf()).ifPresent(cliente -> {
            if (!cliente.getId().equals(id)) {
                throw new IllegalArgumentException("O CPF fornecido já está em uso por outro cliente.");
            }
        });

        // Atualizar os campos do cliente
        clienteExistente.setName(clienteDetailsDTO.getName());
        clienteExistente.setEmail(clienteDetailsDTO.getEmail());
        clienteExistente.setCpf(clienteDetailsDTO.getCpf());
        clienteExistente.setBirthDate(clienteDetailsDTO.getBirthDate());
        clienteExistente.setPhone(clienteDetailsDTO.getPhone());

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return convertToDTO(clienteAtualizado);
    }

    // Buscar todos os clientes
    public List<ClienteDTO> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar cliente por ID
    public ClienteDTO getClienteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id + " não encontrado"));
        return convertToDTO(cliente);
    }

    // Remover cliente
    public void deleteCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id + " não encontrado"));
        clienteRepository.delete(cliente);
    }

    // Converter DTO para Entidade
    private Cliente convertToEntity(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setName(clienteDTO.getName());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setBirthDate(clienteDTO.getBirthDate());
        cliente.setPhone(clienteDTO.getPhone());
        // Mapeie endereços se necessário
        return cliente;
    }

    // Converter Entidade para DTO
    private ClienteDTO convertToDTO(Cliente cliente) {
        if (cliente == null) {
            throw new NullPointerException("Cliente é nulo durante a conversão para DTO");
        }

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setName(cliente.getName());
        clienteDTO.setEmail(cliente.getEmail());
        clienteDTO.setCpf(cliente.getCpf());
        clienteDTO.setBirthDate(cliente.getBirthDate());
        clienteDTO.setPhone(cliente.getPhone());
        // Mapeie endereços se necessário
        return clienteDTO;
    }
}
