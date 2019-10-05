package repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import modelo.clases.Usuario;
import utils.Utils;

public class UsuarioRepository implements Repository<Usuario> {

	@Override
	@SuppressWarnings("unchecked")
	public List<Usuario> all() {
		return entityManager().createQuery("FROM Usuario").getResultList();
	}
	
    public Optional<Usuario> find(long id) {
    	Usuario usuario = (Usuario) entityManager()
                .createQuery("SELECT u FROM Usuario u WHERE u.id = :id")
                .setParameter("id", id)
                .getSingleResult();

        return Optional.ofNullable(usuario);
    }
	
    public Optional<Usuario> find(String username, String password) {
        Query query = entityManager().createQuery("SELECT u FROM Usuario u WHERE u.username = :username AND u.password = :password")
                .setParameter("username", username)
                .setParameter("password", Utils.generarHash256(password))
                .setMaxResults(1);

        try {
            return Optional.of((Usuario) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
