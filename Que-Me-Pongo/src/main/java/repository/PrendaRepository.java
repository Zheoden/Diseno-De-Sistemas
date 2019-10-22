package repository;

import java.util.List;
import java.util.Optional;

import modelo.clases.Prenda;
import modelo.interfaces.Repository;

public class PrendaRepository implements Repository<Prenda> {

	@SuppressWarnings("unchecked")
	@Override
	public List<Prenda> all() {
		return entityManager().createQuery("FROM Prenda").getResultList();
	}

	public Optional<Prenda> find(long id) {
		Prenda prenda = (Prenda) entityManager().createQuery("SELECT p FROM Prenda p WHERE e.id = :id")
				.setParameter("id", id).getSingleResult();

		return Optional.ofNullable(prenda);
	}

	@SuppressWarnings("unchecked")
	public List<Prenda> findAllByGuardarropaId(long guardarropaId) {
		String query = "FROM Guardarropas g JOIN g.prendas p WHERE  g.id = :id";
		List<Prenda> prendas = entityManager().createQuery(query).setParameter("id", guardarropaId).getResultList();
		return prendas;
	}

}
