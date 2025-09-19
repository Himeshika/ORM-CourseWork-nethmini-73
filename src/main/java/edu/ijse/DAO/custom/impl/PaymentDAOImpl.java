package edu.ijse.DAO.custom.impl;

import edu.ijse.DAO.custom.PaymentDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    private FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean update(Payment payment,Session session) {
        try {
            session.merge(payment);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();
        try {
            Payment payment = session.get(Payment.class, id);
            if (payment != null) {
                session.remove(payment);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Payment> getAll() {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();
        try {
            List<Payment> payments = session.createQuery("from Payment", Payment.class).list();
            tx.commit();
            return payments;
        } catch (Exception e) {
            tx.rollback();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    @Override
    public Payment findById(int id,Session session) {
        try {
            Payment payment = session.get(Payment.class, id);
            return payment;
        } catch (Exception e) {
           throw e;
        }
    }

    @Override
    public boolean save(Payment payment, Session session) {
        try {
            session.persist(payment);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
}
