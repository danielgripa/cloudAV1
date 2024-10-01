package br.edu.ibmec.demo.controller;

import br.edu.ibmec.demo.dto.ClienteDTO;
import br.edu.ibmec.demo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Adicionar um novo cliente
    @PostMapping
    public ResponseEntity<ClienteDTO> addCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO novoClienteDTO = clienteService.addCliente(clienteDTO);
        return new ResponseEntity<>(novoClienteDTO, HttpStatus.CREATED);
    }

    // Atualizar cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDetailsDTO) {
        ClienteDTO clienteAtualizadoDTO = clienteService.updateCliente(id, clienteDetailsDTO);
        return ResponseEntity.ok(clienteAtualizadoDTO);
    }

    // Buscar todos os clientes
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        List<ClienteDTO> clientes = clienteService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }

    // Buscar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        ClienteDTO clienteDTO = clienteService.getClienteById(id);
        return ResponseEntity.ok(clienteDTO);
    }

    // Deletar cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }
}
