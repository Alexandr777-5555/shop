package shop.repo.jpa;


import org.springframework.stereotype.Repository;
import shop.model.Customer;
import shop.repo.CustomerRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository("jpaCustomer")
public class JpaCustomerRepository implements CustomerRepository {

    private final EntityManagerFactory entityManagerFactory;

    public JpaCustomerRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    @Override
    public void create(Customer customer) {
        EntityManager manager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            Customer custom = manager.merge(customer);
            transaction.commit();

        } catch (RuntimeException ex) {
            transaction.rollback();
            throw ex;
        } finally {
            manager.close();
        }
    }

    @Override
    public void remove(Customer customer) {
       // TODO
    }

    @Override
    public void remove(long id) {
        EntityManager manager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            Customer customer = manager.find(Customer.class, id);
            manager.remove(customer);
            transaction.commit();
        } catch (RuntimeException ex) {
            transaction.rollback();
            throw ex;
        } finally {
            manager.close();
        }
    }


    @Override
    public Customer find(Customer customer) {
        // TODO
        return null;
    }

    @Override
    public boolean exists(Customer customer) {
        // TODO
        return false;
    }

    @Override
    public List<Customer> findAll() {
        EntityManager manager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Customer> query = manager
                    .createQuery("select customer from Customer customer", Customer.class);
            return query.getResultList();
        } finally {
            manager.close();
        }
    }
}
