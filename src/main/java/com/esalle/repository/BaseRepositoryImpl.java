package com.esalle.repository;

import com.esalle.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Generic Hibernate implementation of BaseRepository
 * @param <T> Entity type
 * @param <ID> Entity ID type
 */
public abstract class BaseRepositoryImpl<T, ID extends Serializable> implements BaseRepository<T, ID> {
    
    protected final SessionFactory sessionFactory;
    protected final Class<T> entityClass;
    
    public BaseRepositoryImpl(Class<T> entityClass) {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        this.entityClass = entityClass;
    }
    
    @Override
    public T save(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving entity", e);
        } finally {
            session.close();
        }
    }
    
    @Override
    public Optional<T> findById(ID id) {
        Session session = sessionFactory.openSession();
        try {
            T entity = session.get(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error finding entity by ID: " + id, e);
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<T> findAll() {
        Session session = sessionFactory.openSession();
        try {
            Query<T> query = session.createQuery("FROM " + entityClass.getSimpleName(), entityClass);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all entities", e);
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean deleteById(ID id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.delete(entity);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting entity by ID: " + id, e);
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean delete(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting entity", e);
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean existsById(ID id) {
        Session session = sessionFactory.openSession();
        try {
            T entity = session.get(entityClass, id);
            return entity != null;
        } catch (Exception e) {
            throw new RuntimeException("Error checking entity existence by ID: " + id, e);
        } finally {
            session.close();
        }
    }
    
    @Override
    public long count() {
        Session session = sessionFactory.openSession();
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM " + entityClass.getSimpleName(), Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error counting entities", e);
        } finally {
            session.close();
        }
    }
}

