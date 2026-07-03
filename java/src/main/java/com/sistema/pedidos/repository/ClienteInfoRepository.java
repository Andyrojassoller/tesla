package com.sistema.pedidos.repository;

import com.sistema.pedidos.document.ClienteInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteInfoRepository extends MongoRepository<ClienteInfo, String> {
    
    Optional<ClienteInfo> findByIdCliente(Integer idCliente);
    
    void deleteByIdCliente(Integer idCliente);
    
    @Query("{ 'preferencias.notificaciones': true }")
    List<ClienteInfo> findClientesConNotificaciones();
    
    @Query("{ 'preferencias.newsletter': true }")
    List<ClienteInfo> findClientesConNewsletter();
    
    @Query("{ 'preferencias.idioma': ?0 }")
    List<ClienteInfo> findByIdioma(String idioma);
}
