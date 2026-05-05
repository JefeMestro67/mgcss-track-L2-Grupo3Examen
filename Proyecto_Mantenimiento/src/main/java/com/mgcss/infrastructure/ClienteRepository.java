package com.mgcss.infrastructure;

import java.util.Optional;
import com.mgcss.domain.Cliente;

public interface ClienteRepository {
    Cliente save(Cliente cliente); 
    Optional<Cliente> findById(Long id);
}
