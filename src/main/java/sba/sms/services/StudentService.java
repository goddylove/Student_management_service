package sba.sms.services;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentService implements StudentI {
    @Override
    public List<Student> getAllStudents() {
        SessionFactory sessionFactory= new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        List<Student> studentList= new ArrayList<>();

        try{
            studentList= session.createQuery("From Student", Student.class).list();
            t.commit();

        }catch (Exception e){
            if (t != null && t.isActive()){
                t.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
            sessionFactory.close();
        }
        return studentList;
    }

    @Override
    public void createStudent(Student student) {
        SessionFactory sessionFactory= new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();

        try {
            session.persist(student);
            t.commit();

        }catch (Exception e){
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
    public Student getStudentByEmail(String email) {
        SessionFactory sessionFactory= new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        Student student= new Student();

        try {
            student = session.createQuery("From Student where email = :email ", Student.class)
                    .setParameter("email", email)
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
        return student;
    }

    @Override
    public boolean validateStudent(String email, String password) {
        SessionFactory sessionFactory= new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        Student student= new Student();
        boolean isValid = false; // Assume student is not valid initially


        try{
            student= session.createQuery("From Student where email = :email and password = :password", Student.class)
                    .setParameter("email", email).setParameter("password", password).uniqueResult();
            if(student !=null){
            isValid= true;
            }
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
        return isValid;
    }

@Override
public void registerStudentToCourse(String email, int courseId) {
    SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    Session session = sessionFactory.openSession();
    Transaction transaction = session.beginTransaction();

    try {
        Student student = session.createQuery("FROM Student WHERE email = :email", Student.class)
                .setParameter("email", email)
                .uniqueResult();
        Course course = session.get(Course.class, courseId);
        if (student != null && course != null) {
            if (!student.getCourses().contains(course)) {
                student.getCourses().add(course);
            }
        }
        transaction.commit();
    } catch (Exception e) {
        // Handle exceptions
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
        e.printStackTrace();
    } finally {
        session.close();
        sessionFactory.close();
    }
}

    @Override
    public List<Course> getStudentCourses(String email) {
        SessionFactory sessionFactory= new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        List<Course> coursesList= new ArrayList<>();

        try{
            String nativeQuery= "Select c.* from Course c " +
                    "inner join student_courses sc on c.id=sc.courses_id " +
                    "INNER JOIN Student s on s.email=sc.student_email " +
                    "where s.email = :email";
            coursesList = session.createNativeQuery(nativeQuery, Course.class)
                    .setParameter("email", email)
                    .list();
            t.commit();

        }catch(Exception e){
            if (t != null && t.isActive()) {
                t.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
            sessionFactory.close();
        }

        return coursesList;
    }
}
