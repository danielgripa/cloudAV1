package br.edu.ibmec.demo.service;

import br.edu.ibmec.demo.dto.EnderecoDTO;
import br.edu.ibmec.demo.exception.BadRequestException;
import br.edu.ibmec.demo.exception.ResourceNotFoundException;
import br.edu.ibmec.demo.model.Cliente;
import br.edu.ibmec.demo.model.Endereco;
import br.edu.ibmec.demo.repository.ClienteRepository;
import br.edu.ibmec.demo.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    // Adicionar novo endereço
    public EnderecoDTO addEndereco(EnderecoDTO enderecoDTO) {
        validarEndereco(enderecoDTO);
        Endereco endereco = convertToEntity(enderecoDTO);
        Endereco novoEndereco = enderecoRepository.save(endereco);
        return convertToDTO(novoEndereco);
    }

    // Atualizar endereço existente
    public EnderecoDTO updateEndereco(Long id, EnderecoDTO enderecoDetailsDTO) {
        validarEndereco(enderecoDetailsDTO);
        Endereco enderecoExistente = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço com ID " + id + " não encontrado"));

        // Atualizar os campos do endereço
        enderecoExistente.setStreet(enderecoDetailsDTO.getStreet());
        enderecoExistente.setNumber(enderecoDetailsDTO.getNumber());
        enderecoExistente.setNeighborhood(enderecoDetailsDTO.getNeighborhood());
        enderecoExistente.setCity(enderecoDetailsDTO.getCity());
        enderecoExistente.setState(enderecoDetailsDTO.getState());
        enderecoExistente.setZipCode(enderecoDetailsDTO.getZipCode());

        Endereco enderecoAtualizado = enderecoRepository.save(enderecoExistente);
        return convertToDTO(enderecoAtualizado);
    }

    // Buscar todos os endereços
    public List<EnderecoDTO> getAllEnderecos() {
        return enderecoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar endereço por ID
    public EnderecoDTO getEnderecoById(Long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço com ID " + id + " não encontrado"));
        return convertToDTO(endereco);
    }

    // Remover endereço
    public void deleteEndereco(Long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço com ID " + id + " não encontrado"));
        enderecoRepository.delete(endereco);
    }

    // Associar endereço a um cliente existente
    public EnderecoDTO adicionarEnderecoAoCliente(Long clienteId, EnderecoDTO enderecoDTO) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + clienteId));

        validarEndereco(enderecoDTO);
        Endereco endereco = convertToEntity(enderecoDTO);
        endereco.setCliente(cliente);

        Endereco novoEndereco = enderecoRepository.save(endereco);
        return convertToDTO(novoEndereco);
    }

    // Validar campos do endereço
    private void validarEndereco(EnderecoDTO enderecoDTO) {
        // Validar presença de campos obrigatórios
        if (enderecoDTO.getStreet() == null || enderecoDTO.getStreet().isEmpty() ||
                enderecoDTO.getNumber() == null || enderecoDTO.getNumber().isEmpty() ||
                enderecoDTO.getNeighborhood() == null || enderecoDTO.getNeighborhood().isEmpty() ||
                enderecoDTO.getCity() == null || enderecoDTO.getCity().isEmpty() ||
                enderecoDTO.getState() == null || enderecoDTO.getState().isEmpty() ||
                enderecoDTO.getZipCode() == null || enderecoDTO.getZipCode().isEmpty()) {
            throw new BadRequestException("Campos obrigatórios ausentes");
        }

        // Validar tamanho dos campos
        if (enderecoDTO.getStreet().length() < 3 || enderecoDTO.getStreet().length() > 255 ||
                enderecoDTO.getNeighborhood().length() < 3 || enderecoDTO.getNeighborhood().length() > 255 ||
                enderecoDTO.getCity().length() < 3 || enderecoDTO.getCity().length() > 255) {
            throw new BadRequestException("Campos rua, bairro ou cidade inválidos (mínimo 3, máximo 255 caracteres)");
        }

        // Validar estado
        List<String> estadosValidos = Arrays.asList("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
                "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");
        if (!estadosValidos.contains(enderecoDTO.getState())) {
            throw new BadRequestException("Estado inválido");
        }

        // Validar formato do CEP
        if (!enderecoDTO.getZipCode().matches("\\d{5}-\\d{3}")) {
            throw new BadRequestException("CEP inválido (formato XXXXX-XXX)");
        }
    }
    public List<EnderecoDTO> getEnderecosByClienteId(Long id) {
        // Buscar cliente pelo ID
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        // Retornar endereços associados ao cliente como DTO
        return cliente.getEnderecos().stream()
                .map(this::convertToDTO) // Certifique-se de que o método de conversão existe
                .collect(Collectors.toList());
    }

    // Converter DTO para Entidade
    public Endereco convertToEntity(EnderecoDTO enderecoDTO) {
        Endereco endereco = new Endereco();
        endereco.setId(enderecoDTO.getId());
        endereco.setStreet(enderecoDTO.getStreet());
        endereco.setNumber(enderecoDTO.getNumber());
        endereco.setNeighborhood(enderecoDTO.getNeighborhood());
        endereco.setCity(enderecoDTO.getCity());
        endereco.setState(enderecoDTO.getState());
        endereco.setZipCode(enderecoDTO.getZipCode());
        return endereco;
    }

    // Converter Entidade para DTO
    public EnderecoDTO convertToDTO(Endereco endereco) {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setId(endereco.getId());
        enderecoDTO.setStreet(endereco.getStreet());
        enderecoDTO.setNumber(endereco.getNumber());
        enderecoDTO.setNeighborhood(endereco.getNeighborhood());
        enderecoDTO.setCity(endereco.getCity());
        enderecoDTO.setState(endereco.getState());
        enderecoDTO.setZipCode(endereco.getZipCode());
        return enderecoDTO;
    }
}
