package br.edu.ibmec.demo.service;

import br.edu.ibmec.demo.dto.EnderecoDTO;
import br.edu.ibmec.demo.exception.ResourceNotFoundException;
import br.edu.ibmec.demo.model.Endereco;
import br.edu.ibmec.demo.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    // Adicionar um novo endereço
    public EnderecoDTO addEndereco(EnderecoDTO enderecoDTO) {
        // Adicione validações específicas de unicidade, se necessário
        Endereco endereco = convertToEntity(enderecoDTO);
        Endereco novoEndereco = enderecoRepository.save(endereco);
        return convertToDTO(novoEndereco);
    }

    // Atualizar endereço existente
    public EnderecoDTO updateEndereco(Long id, EnderecoDTO enderecoDetailsDTO) {
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

    // Converter DTO para Entidade
    private Endereco convertToEntity(EnderecoDTO enderecoDTO) {
        Endereco endereco = new Endereco();
        endereco.setStreet(enderecoDTO.getStreet());
        endereco.setNumber(enderecoDTO.getNumber());
        endereco.setNeighborhood(enderecoDTO.getNeighborhood());
        endereco.setCity(enderecoDTO.getCity());
        endereco.setState(enderecoDTO.getState());
        endereco.setZipCode(enderecoDTO.getZipCode());
        return endereco;
    }

    // Converter Entidade para DTO
    private EnderecoDTO convertToDTO(Endereco endereco) {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setStreet(endereco.getStreet());
        enderecoDTO.setNumber(endereco.getNumber());
        enderecoDTO.setNeighborhood(endereco.getNeighborhood());
        enderecoDTO.setCity(endereco.getCity());
        enderecoDTO.setState(endereco.getState());
        enderecoDTO.setZipCode(endereco.getZipCode());
        return enderecoDTO;
    }
}
