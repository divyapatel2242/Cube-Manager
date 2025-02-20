package com.cube.manage.crm.dbutil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import java.io.Serializable;

public class HibernateAccess {
    @Autowired
    private SessionFactory sessionFactory;

    public <T> T get(Class<T> entityClass, Serializable id){
        StopWatch watch=new StopWatch();
        try{
            sessionFactory.openSession();
            return (T) currentSession().get(entityClass,id);
        }
        catch (Exception e){
            System.out.println("Exception in HibernateConfiguration");
            return (T) e.getCause();
        }
        finally {
            System.out.println("Session Running");
            sessionFactory.close();
        }
    }

    public Session currentSession(){
        return sessionFactory.getCurrentSession();
        //sessionFactory.close();
    }
}
