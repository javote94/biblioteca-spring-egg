
package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.EditorialRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {
    
    @Autowired
    private EditorialRepo er;
    
    @Transactional
    public void crearEditorial(String nombre) throws MiException{
        
        validar(nombre);
        
        Editorial editorial = new Editorial();
        
        editorial.setNombre(nombre);
        
        er.save(editorial);
    }
    
    public List<Editorial> listarEditoriales() {
    
        List<Editorial> editoriales = new ArrayList();
        
        editoriales = er.findAll();
        
        return editoriales;
    }
    
    @Transactional
    public void modificarEditorial(String idEditorial, String nombre, Boolean estado) throws MiException {
        
        validar(nombre);
        
        Optional<Editorial> respuesta = er.findById(idEditorial);
        
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            
            editorial.setNombre(nombre);
            editorial.setEstado(estado);
            
            er.save(editorial);
        }
    }
    
    public Editorial getOne(String id) {
        return er.getOne(id);
    }
    
    private void validar(String nombre) throws MiException{
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre de la editorial no puede estar vacío o nulo");
        }
    }
    
}
