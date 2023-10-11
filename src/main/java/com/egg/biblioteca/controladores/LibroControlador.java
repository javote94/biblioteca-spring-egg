package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.AutorServicio;
import com.egg.biblioteca.servicios.EditorialServicio;
import com.egg.biblioteca.servicios.LibroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")  //localhost:8080/libro
public class LibroControlador {

    @Autowired
    private LibroServicio ls;
    @Autowired
    private AutorServicio as;
    @Autowired
    private EditorialServicio es;

    @GetMapping("/registrar")  //localhost:8080/libro/registrar
    public String registrar(ModelMap modelo) {

        List<Autor> autores = as.listarAutores();
        List<Editorial> editoriales = es.listarEditoriales();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro-form.html";
    }

    @PostMapping("/registro")  //localhost:8080/libro/registro
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor,
            @RequestParam String idEditorial, ModelMap modelo) {

        try {
            ls.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put("exito", "El libro fue cargado correctamente");

        } catch (MiException ex) {
            List<Autor> autores = as.listarAutores();
            List<Editorial> editoriales = es.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            modelo.put("error", ex.getMessage());

            return "libro-form.html";
        }

        return "index.html";
    }

    @GetMapping("/lista")  //localhost:8080/libro/lista
    public String listar(ModelMap modelo) {

        List<Libro> libros = ls.listarLibros();

        modelo.addAttribute("libros", libros);

        return "libro-list.html";
    }

    @GetMapping("/modificar/{isbn}")  //localhost:8080/libro/modificar/isbn
    public String modificar(@PathVariable Long isbn, ModelMap modelo) {

        modelo.put("libro", ls.getOne(isbn));
        
        List<Autor> autores = as.listarAutores();
        List<Editorial> editoriales = es.listarEditoriales();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro-modificar.html";
    }
    
    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, String titulo, Integer ejemplares,
            String idAutor, String idEditorial, Boolean estado, ModelMap modelo) {
        
        try {
            ls.modificarLibro(isbn, titulo, ejemplares, idAutor, idEditorial, estado);
            return "redirect:../lista";
            
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "libro-modificar.html";
        }
    }
}
