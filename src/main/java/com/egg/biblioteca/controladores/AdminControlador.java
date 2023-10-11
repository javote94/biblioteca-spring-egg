

package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/*
Debemos aplicarle seguridad a toda la clase generando una preautorización a 
nivel clase desde el método configure() en la clase SeguridadWeb
*/
@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    private UsuarioServicio us;
    
    @GetMapping("/dashboard")            //localhost:8080/admin/dashboard
    public String panelAdmin(){
        return "panel.html";
    }
    
    @GetMapping("/usuarios")             //localhost:8080/admin/usuarios
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = us.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario-list.html";
    }
    
    
    @GetMapping("/modificarRol/{id}")     //localhost:8080/admin/modificarRol/idUsuario
    public String cambiarRol(@PathVariable String id) {
        us.cambiarRol(id);
        return "redirect:/admin/usuarios";
    }
    
}
