//Se realizan las importaciones del modelo usuario, el repositorio de los usuarios  para llevar a cabo la manipulacion
//De la base de datos
package tutorial.misionTIC.ModuloSeguridad.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Rol;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Usuario;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioRol;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioUsuario;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

//CrossOrigin permite que se lleven a cabo las tansacciones al servidor desde el localhost.
@CrossOrigin

//RestController permite la entrada al servidor para llevar las peticiones del CRUD (POST, GET, PUT, DELETE)
@RestController

//Definimos la subruta donde activaremos los metodos programados de la presente clase.
@RequestMapping("/usuarios")
public class ControladorUsuario {

    //usamos una inyeccion de setter para la dependencia del objeto.
    @Autowired
    private RepositorioUsuario miRepositorioUsuario;
    @Autowired
    private RepositorioRol miRepositorioRol;

    //Creamos un metodo para listar todos los usuarios
    @GetMapping("")
    public List<Usuario> index(){
        return this.miRepositorioUsuario.findAll();
    }
    @ResponseStatus(HttpStatus.CREATED)

    //Metodo para Crear Usuario
    @PostMapping
    public Usuario create(@RequestBody  Usuario infoUsuario){
        infoUsuario.setContrasena(convertirSHA256(infoUsuario.getContrasena()));
        return this.miRepositorioUsuario.save(infoUsuario);
    }
    //Metodo para Mostrar usuario por Id
    @GetMapping("{id}")
    public Usuario show(@PathVariable String id){
        Usuario usuarioActual=this.miRepositorioUsuario
                .findById(id)
                .orElse(null);
        return usuarioActual;
    }
    //Metodo para modificar un Usuario
    @PutMapping("{id}")
    public Usuario update(@PathVariable String id,@RequestBody  Usuario infoUsuario){
        Usuario usuarioActual=this.miRepositorioUsuario
                .findById(id)
                .orElse(null);
        //Si el usuario es diferente de null Se procede a actualizar los datos
        if (usuarioActual!=null){
            usuarioActual.setSeudonimo(infoUsuario.getSeudonimo());
            usuarioActual.setCorreo(infoUsuario.getCorreo());
            usuarioActual.setContrasena(convertirSHA256(infoUsuario.getContrasena()));
            return this.miRepositorioUsuario.save(usuarioActual);
        }else{
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    //Metodo para eliminar un usuario
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Usuario usuarioActual=this.miRepositorioUsuario
                .findById(id)
                .orElse(null);
        if (usuarioActual!=null){
            this.miRepositorioUsuario.delete(usuarioActual);
        }
    }
    /**
     * Relación (1 a n) entre rol y usuario
     * @param id
     * @param id_rol
     * @return
     */

    //Creamos el metodo para relacionar el usuario con el rol
    @PutMapping("{id}/rol/{id_rol}")
    public Usuario asignarRolAUsuario(@PathVariable String id,@PathVariable String id_rol){
        Usuario usuarioActual=this.miRepositorioUsuario
                .findById(id)
                .orElse(null);
        Rol rolActual=this.miRepositorioRol
                .findById(id_rol)
                .orElse(null);
        if (usuarioActual!=null && rolActual!=null){
            usuarioActual.setRol(rolActual);
            return this.miRepositorioUsuario.save(usuarioActual);
        }else{
            return null;
        }

    }
    //Metodo para cifrar las claves con el formato SHA256
    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}