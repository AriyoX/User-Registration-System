package org.pahappa.systems.registrationapp.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;

import java.util.List;

public class DependantDAO {
    private final SessionFactory sessionFactory;

    public DependantDAO() {
        this.sessionFactory = SessionConfiguration.getSessionFactory();
    }

    public boolean isDatabaseConnected(){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery("SELECT 1 FROM Dependants").uniqueResult();
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void addDependantToUser(User user, Dependant dependant){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            User existingUser = (User) session.get(User.class, user.getId());
            if (existingUser == null){
               throw new IllegalArgumentException("User does not exist");
            }
            dependant.setUser(user);
            session.save(dependant);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Dependant> getUserDependants(User user){
        Transaction transaction = null;
        Session session = null;
        List<Dependant> dependants = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            dependants = (List<Dependant>) session.createQuery("FROM Dependant WHERE user.id = :userId")
                    .setParameter("userId", user.getId())
                    .list();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return dependants;
    }

    public void delete(Dependant dependant){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(dependant);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Dependant> getAllDependants(){
        Transaction transaction = null;
        Session session = null;
        List<Dependant> dependants = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            dependants = (List<Dependant>) session.createQuery("FROM Dependant").list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return dependants;
    }

}
