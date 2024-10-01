package br.edu.ibmec.demo.repository;

import br.edu.ibmec.demo.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
