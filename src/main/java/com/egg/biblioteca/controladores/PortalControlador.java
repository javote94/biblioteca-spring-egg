package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio us;

    @GetMapping("/")               //localhost:8080/
    public String index() {
        return "index.html";
    }

    @GetMapping("/registrar")      //localhost:8080/registrar
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registro")      //localhost:8080/registro
    public String registro(@RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2,
            MultipartFile archivo, ModelMap modelo) throws Exception {

        try {
            us.regitrar(nombre, email, password, password2, archivo);
            modelo.put("exito", "Usuario registrado correctamente");
            return "index.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());

            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro.html";
        }
    }

    @GetMapping("/login")          //localhost:8080/login
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o contraseña incorrecta");
        }
        return "login.html";
    }

    //Se requiere autorización para poder ingresar al método controlador que retorna la vista de inicio para el usuario logueado
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')") //El usuario debe tener alguno de los dos roles para acceder a la vista inicio.html
    @GetMapping("/inicio")         //localhost:8080/inicio
    public String inicio(HttpSession session) {

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuariosession");

        if (usuarioLogueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "inicio.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuarioLogueado);
        return "usuario-modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizarPerfil(@PathVariable String id, @RequestParam String nombre,
            @RequestParam String email, @RequestParam String password, @RequestParam String password2,
            MultipartFile archivo, ModelMap modelo) throws MiException, Exception {

        try {
            us.actualizar(id, nombre, email, password, password2, archivo);
            modelo.put("exito", "Usuario actualizado correctamente");
            return "inicio.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario-modificar.html";
        }
    }

}
