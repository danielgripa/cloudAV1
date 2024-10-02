package br.edu.ibmec.demo.controller;

import br.edu.ibmec.demo.dto.EnderecoDTO;
import br.edu.ibmec.demo.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    // Adicionar um novo endereço
    @PostMapping
    public ResponseEntity<EnderecoDTO> addEndereco(@Valid @RequestBody EnderecoDTO enderecoDTO) {
        EnderecoDTO novoEnderecoDTO = enderecoService.addEndereco(enderecoDTO);
        return new ResponseEntity<>(novoEnderecoDTO, HttpStatus.CREATED);
    }

    // Atualizar endereço existente
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoDTO> updateEndereco(@PathVariable Long id, @Valid @RequestBody EnderecoDTO enderecoDetailsDTO) {
        EnderecoDTO enderecoAtualizadoDTO = enderecoService.updateEndereco(id, enderecoDetailsDTO);
        return ResponseEntity.ok(enderecoAtualizadoDTO);
    }

    // Buscar todos os endereços
    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> getAllEnderecos() {
        List<EnderecoDTO> enderecos = enderecoService.getAllEnderecos();
        return ResponseEntity.ok(enderecos);
    }

    // Buscar endereço por ID
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoDTO> getEnderecoById(@PathVariable Long id) {
        EnderecoDTO enderecoDTO = enderecoService.getEnderecoById(id);
        return ResponseEntity.ok(enderecoDTO);
    }

    // Deletar endereço por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndereco(@PathVariable Long id) {
        enderecoService.deleteEndereco(id);
        return ResponseEntity.noContent().build();
    }

}
