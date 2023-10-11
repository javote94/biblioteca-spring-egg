package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.UsuarioRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepo ur;

    @Autowired
    private ImagenServicio is;

    @Transactional
    public void regitrar(String nombre, String email, String password, String password2, MultipartFile archivo) throws MiException, Exception {

        validar(nombre, email, password, password2);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);

        Imagen imagen = is.guardar(archivo);

        usuario.setImagen(imagen);

        ur.save(usuario);
    }

    @Transactional
    public void actualizar(String id, String nombre, String email, String password, String password2, MultipartFile archivo) throws MiException, Exception {

        validar(nombre, email, password, password2);

        Optional<Usuario> respuesta = ur.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setRol(Rol.USER);
            
            String idImagen = null;
            
            if (usuario.getImagen() != null) {
               idImagen = usuario.getImagen().getId();
            }
            
            Imagen imagen = is.actualizar(archivo, idImagen);
            
            usuario.setImagen(imagen);
            
            ur.save(usuario);
        }
    }
    
    public Usuario getOne(String id) {
        return ur.getOne(id);
    }
    
    public List<Usuario> listarUsuarios() {
        return ur.findAll();
    }
    
    @Transactional
    public void cambiarRol(String id) {
        
        Optional<Usuario> respuesta = ur.findById(id);
        
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            
            if (usuario.getRol().equals(Rol.USER)) {
                usuario.setRol(Rol.ADMIN);
                
            } else if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USER);
            }
        }
    }

    private void validar(String nombre, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre de usuario no puede estar vacío o nulo");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede estar vacío o nulo");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }

    }

    /*
    Cuando un usuario se logee (es decir, inicie sesión con sus credenciales)
    Spring Security va a dirigirse directamente a este método y va a otorgar
    los permisos a los que tiene acceso el usuario.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = ur.buscarPorEmail(email);

        if (usuario != null) {  //Verificamos que el objeto Usuario no esté nulo.

            List<GrantedAuthority> permisos = new ArrayList(); //Creamos una lista de permisos

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());  //Creamos un objeto GrantedAuthority 'p' y concatenamos la palabra ROLE_ + el rol del usuario.

            permisos.add(p);  //Agregamos el objeto ‘p’ a la lista de permisos.

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        } else {
            return null;
        }
    }

    

    
}
