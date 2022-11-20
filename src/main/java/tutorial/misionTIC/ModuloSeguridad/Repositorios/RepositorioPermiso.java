package tutorial.misionTIC.ModuloSeguridad.Repositorios;

import org.springframework.data.mongodb.repository.MongoRepository;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Permiso;

public interface RepositorioPermiso extends MongoRepository<Permiso,String> {
}
