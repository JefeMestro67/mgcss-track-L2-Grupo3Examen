package com.mgcss.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTecnicoRepository extends JpaRepository<TecnicoEntity, Long> {
}
