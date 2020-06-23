package shop.repo.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import shop.domain.Customer;
import shop.repo.CustomerRepository;

import java.util.List;


@Transactional
public class HibernateCustomerRepo implements CustomerRepository {


    @Autowired
    private final SessionFactory sessionFactory;


    public HibernateCustomerRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

    }


    @Override

    public Customer add(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.save(customer);
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public void remove(long id) {

    }

    @Override
    public Customer findOne(long id) {
        return (Customer)
                this.sessionFactory.getCurrentSession().get(Customer.class, id);
    }


}
