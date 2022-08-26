package org.hibernate.bugs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.bugs.entities.Entity1;
import org.hibernate.bugs.entities.Entity2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		createAndPersistEntity(entityManager);
		removeEntity2_1(entityManager);
		int count = countEntity2(entityManager);

		entityManager.close();

		Assertions.assertEquals(1, count);
	}



	private void createAndPersistEntity(EntityManager s) {

		s.getTransaction().begin();

		Entity1 e1 = new Entity1();
		e1.setId("1");
		Entity2 e2_1 = new Entity2();
		e2_1.setId("2_1");
		Entity2 e2_2 = new Entity2();
		e2_2.setId("2_2");
		e1.getEntity2().add(e2_1);
		e1.getEntity2().add(e2_2);
		e2_1.setEntity1(e1);
		e2_2.setEntity1(e1);

		s.persist(e1);

		s.getTransaction().commit();
	}

	private void removeEntity2_1(EntityManager s) {
		s.getTransaction().begin();

		final Entity1 e1 = s.createQuery("select e from Entity1 e", Entity1.class).getResultList().get(0);
		Entity2 e2_1 = e1.getEntity2().get(0);

		// deleted object would be re-saved by cascade (remove deleted object from associations)
		// e1.getEntity2().remove(0);

		s.remove(e2_1);

		s.flush();
		s.getTransaction().commit();
	}

	private int countEntity2(EntityManager s) {
		s.getTransaction().begin();
		final Entity1 e1 = s.createQuery("select e from Entity1 e", Entity1.class).getResultList().get(0);
		s.getTransaction().commit();
		return e1.getEntity2().size();
	}

}