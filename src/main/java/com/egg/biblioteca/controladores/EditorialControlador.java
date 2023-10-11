package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.EditorialServicio;
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
@RequestMapping("/editorial")  //localhost:8080/editorial
public class EditorialControlador {

    @Autowired
    private EditorialServicio es;

    @GetMapping("/registrar")  //localhost:8080/editorial/registrar
    public String registrar(ModelMap modelo) {
        return "editorial-form.html";
    }

    @PostMapping("/registro")  //localhost:8080/editorial/registro
    public String registro(@RequestParam String nombre, ModelMap modelo) {

        try {
            es.crearEditorial(nombre);
            modelo.put("exito", "La editorial fue cargada correctamente");

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "editorial-form.html";
        }

        return "index.html";
    }

    @GetMapping("/lista")  //localhost:8080/editorial/lista
    public String listar(ModelMap modelo) {

        List<Editorial> editoriales = es.listarEditoriales();

        modelo.addAttribute("editoriales", editoriales);

        return "editorial-list.html";
    }

    @GetMapping("/modificar/{id}")  //localhost:8080/editorial/modificar/idEditorial
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("editorial", es.getOne(id));

        return "editorial-modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, Boolean estado, ModelMap modelo) {

        try {
            es.modificarEditorial(id, nombre, estado);
            return "redirect:../lista";
            
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "editorial-modificar.html";
        }

    }
}
