
package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.AutorRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {
    
    @Autowired
    private AutorRepo ar;
    
    @Transactional
    public void crearAutor(String nombre) throws MiException{
        
        validar(nombre);
        
        Autor autor = new Autor();
        
        autor.setNombre(nombre);
        
        ar.save(autor);
    }
    
    public List<Autor> listarAutores() {
    
        List<Autor> autores = new ArrayList();
        
        autores = ar.findAll();
        
        return autores;
    }
    
    @Transactional
    public void modificarAutor(String idAutor, String nombre, Boolean estado) throws MiException{
        
        validar(nombre);
        
        Optional<Autor> respuesta = ar.findById(idAutor);
        
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            
            autor.setNombre(nombre);
            autor.setEstado(estado);
            
            ar.save(autor);
        }
    }
    
    public Autor getOne(String id) {
        return ar.getOne(id);
    }
    
    private void validar(String nombre) throws MiException{
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre del autor no puede estar vacío o nulo");
        }
    }
}
