package org.hibernate.bugs;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.bugs.entities.Entity1;
import org.hibernate.bugs.entities.Entity2;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ORMUnitTestCase extends BaseCoreFunctionalTestCase {

	// Add your entities here.
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Entity1.class,
				Entity2.class
		};
	}

	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
		//configuration.setProperty( AvailableSettings.GENERATE_STATISTICS, "true" );
	}

	//////////////////////////////////////////////////////////////////

	@Test
	public void testcase() throws Exception {
		Session s = openSession();
		createAndPersistEntity(s);
		removeEntity2_1(s);
		int count = countEntity2(s);
		s.close();

		Assertions.assertEquals(1, count);
	}

	//////////////////////////////////////////////////////////////////

	private void createAndPersistEntity(Session s) {
		Transaction tx = s.beginTransaction();

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

		tx.commit();
	}

	private void removeEntity2_1(Session s) {
		Transaction tx = s.beginTransaction();

		final Entity1 e1 = s.createQuery("select e from Entity1 e", Entity1.class).getResultList().get(0);
		Entity2 e2_1 = e1.getEntity2().get(0);

		// deleted object would be re-saved by cascade (remove deleted object from associations)
		// e1.getEntity2().remove(0);

		s.remove(e2_1);

		tx.commit();
	}

	private int countEntity2(Session s) {
		Transaction tx = s.beginTransaction();
		final Entity1 e1 = s.createQuery("select e from Entity1 e", Entity1.class).getResultList().get(0);
		tx.commit();
		return e1.getEntity2().size();
	}

}
