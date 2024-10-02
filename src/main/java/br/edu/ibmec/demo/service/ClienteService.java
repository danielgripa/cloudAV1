package br.edu.ibmec.demo.service;

import br.edu.ibmec.demo.dto.ClienteDTO;
import br.edu.ibmec.demo.dto.EnderecoDTO;
import br.edu.ibmec.demo.exception.ResourceNotFoundException;
import br.edu.ibmec.demo.exception.BadRequestException;
import br.edu.ibmec.demo.model.Cliente;
import br.edu.ibmec.demo.model.Endereco;
import br.edu.ibmec.demo.repository.ClienteRepository;
import br.edu.ibmec.demo.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoService enderecoService;

    // Adicionar um novo cliente
    public ClienteDTO addCliente(ClienteDTO clienteDTO) {
        // Validar idade mínima
        if (!clienteDTO.isAgeValid()) {
            throw new BadRequestException("O cliente deve ter pelo menos 18 anos.");
        }

        // Validação de unicidade do email
        if (clienteRepository.findByEmail(clienteDTO.getEmail()).isPresent()) {
            throw new BadRequestException("O email fornecido já está em uso.");
        }

        // Validação de unicidade do CPF
        if (clienteRepository.findByCpf(clienteDTO.getCpf()).isPresent()) {
            throw new BadRequestException("O CPF fornecido já está em uso.");
        }

        // Converter DTO para entidade
        Cliente cliente = convertToEntity(clienteDTO);

        // Associar endereços ao cliente
        List<Endereco> enderecos = cliente.getEnderecos();
        if (enderecos != null) {
            enderecos.forEach(endereco -> endereco.setCliente(cliente));
        }

        // Salvar cliente
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
                throw new BadRequestException("O email fornecido já está em uso por outro cliente.");
            }
        });

        // Verificar unicidade do CPF para outro cliente
        clienteRepository.findByCpf(clienteDetailsDTO.getCpf()).ifPresent(cliente -> {
            if (!cliente.getId().equals(id)) {
                throw new BadRequestException("O CPF fornecido já está em uso por outro cliente.");
            }
        });

        // Atualizar apenas os campos do cliente, sem alterar endereços
        clienteExistente.setName(clienteDetailsDTO.getName());
        clienteExistente.setEmail(clienteDetailsDTO.getEmail());
        clienteExistente.setCpf(clienteDetailsDTO.getCpf());
        clienteExistente.setBirthDate(clienteDetailsDTO.getBirthDate());
        clienteExistente.setPhone(clienteDetailsDTO.getPhone());

        // Salvar o cliente atualizado
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

    // Associar endereço existente ao cliente
    public void addExistingEnderecoToCliente(Long clienteId, Long enderecoId) {
        // Buscar cliente pelo ID
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + clienteId));

        // Buscar endereço pelo ID
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com o ID: " + enderecoId));

        // Associar o endereço ao cliente
        endereco.setCliente(cliente);

        // Salvar o endereço atualizado
        enderecoRepository.save(endereco);
    }

    // Remover endereço do cliente
    public void removeEnderecoFromCliente(Long clienteId, Long enderecoId) {
        // Buscar cliente pelo ID
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + clienteId));

        // Buscar o endereço associado ao cliente
        Endereco enderecoToRemove = cliente.getEnderecos().stream()
                .filter(endereco -> endereco.getId().equals(enderecoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com o ID: " + enderecoId));

        // Desassociar o endereço do cliente
        enderecoToRemove.setCliente(null);

        // Salvar a alteração no endereço
        enderecoRepository.save(enderecoToRemove);
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

        // Converter a lista de Endereços
        List<EnderecoDTO> enderecosDTO = cliente.getEnderecos() != null
                ? cliente.getEnderecos().stream()
                .map(endereco -> enderecoService.convertToDTO(endereco))
                .collect(Collectors.toList())
                : null;
        clienteDTO.setEnderecos(enderecosDTO);

        return clienteDTO;
    }

    // Converter DTO para Entidade
    private Cliente convertToEntity(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setName(clienteDTO.getName());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setBirthDate(clienteDTO.getBirthDate());
        cliente.setPhone(clienteDTO.getPhone());

        // Converter a lista de Endereços
        List<Endereco> enderecos = clienteDTO.getEnderecos() != null
                ? clienteDTO.getEnderecos().stream()
                .map(enderecoDTO -> enderecoService.convertToEntity(enderecoDTO))
                .collect(Collectors.toList())
                : null;
        cliente.setEnderecos(enderecos);

        return cliente;
    }
}
