package modelo.interfaces;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public interface Repository<T> extends WithGlobalEntityManager, TransactionalOps {
	default void persist(T entity) {
		withTransaction(() -> getEntityManager().persist(entity));
	}

	@SuppressWarnings("unchecked")
	default void persistAll(T... entities) {
		withTransaction(() -> {
			Arrays.stream(entities).forEach(getEntityManager()::persist);
		});
	}

	default void delete(T entity) {
		withTransaction(() -> getEntityManager().remove(entity));
	}

	default void update(T entity) {
		beginTransaction();
		getEntityManager().flush();
		commitTransaction();
	}

	List<T> all();

	public static EntityManager getEntityManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("db");
	    return emf.createEntityManager();
	}
}
