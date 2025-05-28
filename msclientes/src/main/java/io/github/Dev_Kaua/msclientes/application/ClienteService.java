package io.github.Dev_Kaua.msclientes.application;

import io.github.Dev_Kaua.msclientes.domain.Cliente;
import io.github.Dev_Kaua.msclientes.infra.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    @Transactional
    public Cliente save(Cliente cliente){
        return repository.save(cliente);
    }

    public Optional<Cliente> getByCpf(String cpf){
        return repository.findByCpf(cpf);
    }
}
