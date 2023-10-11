
package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.repositorios.LibroRepo;
import com.egg.biblioteca.repositorios.AutorRepo;
import com.egg.biblioteca.repositorios.EditorialRepo;
import com.egg.biblioteca.excepciones.MiException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {
    
    @Autowired
    private LibroRepo lr;
    @Autowired
    private AutorRepo ar;
    @Autowired
    private EditorialRepo er;
    
    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {
        
        validarDatos(isbn, titulo, ejemplares, idAutor, idEditorial);
        
        Autor autor = ar.findById(idAutor).get();
        Editorial editorial = er.findById(idEditorial).get();
        
        Libro libro = new Libro();
        
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        
        lr.save(libro);
    }
    
    public List<Libro> listarLibros() {
        
        List<Libro> libros = new ArrayList();
        
        libros = lr.findAll();
        
        return libros;
    }
    
    @Transactional
    public void modificarLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial, Boolean estado) throws MiException {
        
        validarDatos(isbn, titulo, ejemplares, idAutor, idEditorial);
        
        Optional<Libro> respuestaLibro = lr.findById(isbn);
        Optional<Autor> respuestaAutor = ar.findById(idAutor);
        Optional<Editorial> respuestaEditorial = er.findById(idEditorial);
        
        Autor autor = new Autor();
        Editorial editorial = new Editorial();
        
        if (respuestaAutor.isPresent()) {
            autor = respuestaAutor.get();
        }
        
        if (respuestaEditorial.isPresent()) {
            editorial = respuestaEditorial.get();
        }
        
        if (respuestaLibro.isPresent()) {
            
            Libro libro = respuestaLibro.get();
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libro.setEstado(estado);
            
            lr.save(libro);
        }
    }
    
    public Libro getOne(Long isbn) {
        return lr.getOne(isbn);
    }
    
    private void validarDatos(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {
        
        if (isbn == null) {
            throw new MiException("El isbn no puede ser nulo");
        }
        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("El título no puede estar vacío o nulo");
        }
        if (ejemplares == null) {
            throw new MiException("Los ejemplares no pueden ser nulo");
        }
        if (idAutor.isEmpty() || idAutor == null) {
            throw new MiException("El id del autor no puede estar vacío o nulo");
        }
        if (idEditorial.isEmpty() || idEditorial == null) {
            throw new MiException("El id de la editorial no puede estar vacío o nulo");
        }
    }
    
}
