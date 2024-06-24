package org.pahappa.systems.registrationapp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.Dependant.Gender;
import org.pahappa.systems.registrationapp.models.User;

import java.util.Date;
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

//    public void delete(Dependant dependant){
//        Transaction transaction = null;
//        Session session = null;
//        try {
//            session = sessionFactory.openSession();
//            transaction = session.beginTransaction();
//            session.delete(dependant);
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//    }

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

    public Dependant getDependantByUsername(String username){
        Transaction transaction = null;
        Session session = null;
        Dependant dependant = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            dependant = (Dependant) session.createQuery("FROM Dependant WHERE username = :username")
                    .setParameter("username", username)
                    .uniqueResult();
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
        return dependant;
    }

    public Dependant getDependantByGender(Gender gender){
        Transaction transaction = null;
        Session session = null;
        Dependant dependant = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            dependant = (Dependant) session.createQuery("FROM Dependant WHERE gender = :gender")
                    .setParameter("gender", gender)
                    .uniqueResult();
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
        return dependant;
    }

    public Dependant getDependantByFirstName(String firstname){
        Transaction transaction = null;
        Session session = null;
        Dependant dependant = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            dependant = (Dependant) session.createQuery("FROM Dependant WHERE firstname = :firstname")
                    .setParameter("firstname", firstname)
                    .uniqueResult();
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
        return dependant;
    }

    public Dependant getDependantByLastName(String lastname){
        Transaction transaction = null;
        Session session = null;
        Dependant dependant = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            dependant = (Dependant) session.createQuery("FROM Dependant WHERE lastname = :lastname")
                    .setParameter("lastname", lastname)
                    .uniqueResult();
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
        return dependant;
    }

    public List<Dependant> getDependantsByUserId(long user_id){
        Transaction transaction = null;
        Session session = null;
        List<Dependant> dependants = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            dependants = (List<Dependant>) session.createQuery("FROM Dependant WHERE user.id = :user_id")
                    .setParameter("user_id", user_id)
                    .list();
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

    public void delete(Dependant dependant){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            dependant = (Dependant) session.get(Dependant.class, dependant.getId());
            dependant.setDeleted(true); // Mark as deleted
            dependant.setDeletedAt(new Date());
            session.update(dependant);   // Update the entity
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

}
