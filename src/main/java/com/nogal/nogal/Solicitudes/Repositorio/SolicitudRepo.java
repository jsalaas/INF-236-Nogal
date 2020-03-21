package com.nogal.nogal.Solicitudes.Repositorio;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.nogal.nogal.Solicitudes.Modelos.Solicitud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepo extends JpaRepository<Solicitud, Serializable>{

    Solicitud findByID(long ID);
    public abstract List<Solicitud> findByTecnicoOrCliente(long tecnico, long cliente);

}