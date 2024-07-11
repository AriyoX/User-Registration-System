package org.pahappa.systems.registrationapp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import org.pahappa.systems.registrationapp.models.User;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserDAO {
    private final SessionFactory sessionFactory;

    public UserDAO() {
        this.sessionFactory = SessionConfiguration.getSessionFactory();
    }

//    public boolean isDatabaseConnected() {
//        Transaction transaction = null;
//        Session session = null;
//        try {
//            session = sessionFactory.openSession();
//            transaction = session.beginTransaction();
//            session.createSQLQuery("SELECT 1 FROM Users").uniqueResult();
//            transaction.commit();
//            return true;
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            return false;
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//    }

    public void add(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(user);
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

    public void update(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(user);
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

//    public void delete(User user) {
//        Transaction transaction = null;
//        Session session = null;
//        try {
//            session = sessionFactory.openSession();
//            transaction = session.beginTransaction();
//            session.delete(user);
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

    public User getUserByUsername(String username) {
        Transaction transaction = null;
        Session session = null;
        User user = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            user = (User) session.createQuery("FROM User WHERE username = :username and deleted = false")
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
        return user;
    }

    public User getDeletedUserByUsername(String username) {
        Transaction transaction = null;
        Session session = null;
        User user = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            user = (User) session.createQuery("FROM User WHERE username = :username and deleted = true")
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
        return user;
    }

    public User getUserByFirstName(String firstname) {
        Transaction transaction = null;
        Session session = null;
        User user = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            user = (User) session.createQuery("FROM User WHERE firstname = :firstname and deleted = false")
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
        return user;
    }

    public User getUserByLastName(String lastname) {
        Transaction transaction = null;
        Session session = null;
        User user = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            user = (User) session.createQuery("FROM User WHERE lastname = :lastname and deleted = false")
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
        return user;
    }

    public List<User> getAllUsers() {
        Transaction transaction = null;
        Session session = null;
        List<User> users = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            users = session.createQuery("from User u where u.role != :role and deleted = false")
                    .setParameter("role", "ADMIN")
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
        if (users == null) {
            return Collections.emptyList();
        }
        return users;
    }

    public List<User> getDeletedUsers() {
        Transaction transaction = null;
        Session session = null;
        List<User> users = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            users = session.createQuery("from User u where u.role != :role and deleted = true")
                    .setParameter("role", "ADMIN")
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
        if (users == null) {
            return Collections.emptyList();
        }
        return users;
    }

    public List<User> getAdminUsers() {
        Transaction transaction = null;
        Session session = null;
        List<User> users = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            users = session.createQuery("from User u where u.role = :role and deleted = false")
                    .setParameter("role", "ADMIN")
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
        if (users == null) {
            return Collections.emptyList();
        }
        return users;
    }

    public void deleteAllUsers() {
        Transaction transaction = null;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.createQuery("update Dependant set deleted = true, deletedAt = :deletedAt where deleted = false")
                    .setParameter("deletedAt", (new Date()))
                    .executeUpdate();
            session.createQuery("update User set deleted = true, deletedAt = :deletedAt where role != :role and deleted = false")
                    .setParameter("role", "ADMIN")
                    .setParameter("deletedAt", (new Date()))
                    .executeUpdate();
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

    public User getUserById(long userId){
        Transaction transaction = null;
        Session session = null;
        User user = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            user = (User) session.createQuery("from User where id = :userId and deleted = false")
                    .setParameter("userId", userId)
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
        return user;
    }

    public User getUserByEmail(String email) {
        Transaction transaction = null;
        Session session = null;
        User user = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            user = (User) session.createQuery("from User where email = :email and deleted = false")
                    .setParameter("email", email)
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
        return user;
    }

    public void delete(User user){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            user = (User) session.get(User.class, user.getId());
            user.setDeleted(true);
            user.setDeletedAt(new Date());
            session.update(user);
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

    public List<User> getUsersWithActiveDependants() {
        Transaction transaction = null;
        Session session = null;
        List<User> users = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            users = session.createQuery(
                            "SELECT DISTINCT u FROM User u JOIN u.dependants d WHERE d.deleted = false and u.role != :role and u.deleted = false")
                    .setParameter("role", "ADMIN")
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
        if (users == null) {
            return Collections.emptyList();
        }
        return users;
    }

    public List<User> getUsersWithoutDependants() {
        Transaction transaction = null;
        Session session = null;
        List<User> users = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // Use HQL to select users who do not have any dependants
            users = session.createQuery(
                            "SELECT u FROM User u WHERE u.dependants IS EMPTY and u.role != :role and u.deleted = false")
                    .setParameter("role", "ADMIN")
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
        if (users == null){
            return Collections.emptyList();
        }
        return users;
    }

    public void restoreDeletedUser(String username){
        Transaction transaction = null;
        Session session = null;
        User user = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            user = (User) session.createQuery("FROM User WHERE username = :username and deleted = true")
                    .setParameter("username", username)
                    .uniqueResult();
            user.setDeleted(false);
            user.setDeletedAt(null);
            session.update(user);
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
