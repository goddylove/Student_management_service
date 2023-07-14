
package sba.sms.services;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseService implements CourseI {

    @Override
    public void createCourse(Course course) {

        SessionFactory sessionFactory= new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();

        try {
            session.persist(course);
            t.commit();

        } catch (Exception e) {
            if (t != null && t.isActive()){
                t.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
            sessionFactory.close();
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        SessionFactory sessionFactory= new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        Course course = new Course();

        try {
            course = session.createQuery("From Course where id = :courseId ", Course.class)
                    .setParameter("courseId", courseId)
                    .uniqueResult();
            t.commit();

        } catch (Exception e) {
            if (t != null && t.isActive()){
                t.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
            sessionFactory.close();
        }
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        SessionFactory sessionFactory= new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        List<Course> courseList = new ArrayList<>();

        try {
            courseList = session.createQuery("From Course", Course.class).list();
            t.commit();

        } catch (Exception e){
            if (t != null && t.isActive()){
                t.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
            sessionFactory.close();
        }
        return courseList;
    }
}
