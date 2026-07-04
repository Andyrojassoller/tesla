package com.sistema.pedidos.service;

import com.sistema.pedidos.document.HistorialNavegacion;
import com.sistema.pedidos.entity.Producto;
import com.sistema.pedidos.entity.Usuario;
import com.sistema.pedidos.repository.HistorialNavegacionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialNavegacionService {
    
    private final HistorialNavegacionRepository historialRepository;
    
    public void registrarVisita(Usuario usuario, Producto producto, HttpServletRequest request) {
        HistorialNavegacion historial = new HistorialNavegacion();
        historial.setIdUsuario(usuario.getIdUsuario());
        historial.setNombreUsuario(usuario.getNombreUsuario());
        historial.setIdProducto(producto.getIdProducto());
        historial.setNombreProducto(producto.getNombre());
        historial.setCategoriaProducto(producto.getCategoria());
        historial.setFechaVisita(LocalDateTime.now());
        historial.setIpAddress(getClientIp(request));
        historial.setUserAgent(request.getHeader("User-Agent"));
        
        historialRepository.save(historial);
    }
    
    public List<HistorialNavegacion> obtenerHistorialUsuario(Integer idUsuario) {
        return historialRepository.findByIdUsuarioOrderByFechaVisitaDesc(idUsuario);
    }
    
    public List<HistorialNavegacion> obtenerUltimasVisitas(Integer idUsuario, int cantidad) {
        return historialRepository.findTop10ByIdUsuarioOrderByFechaVisitaDesc(idUsuario);
    }
    
    public List<HistorialNavegacion> obtenerVisitasProducto(Integer idProducto) {
        return historialRepository.findByIdProductoOrderByFechaVisitaDesc(idProducto);
    }
    
    public Long contarVisitasProductoPorUsuario(Integer idUsuario, Integer idProducto) {
        return historialRepository.countByIdUsuarioAndIdProducto(idUsuario, idProducto);
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
