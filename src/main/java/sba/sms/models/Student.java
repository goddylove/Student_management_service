package sba.sms.models;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @Column(length = 50, name= "email", nullable = false, unique = true)
    private String email;
    @Column(length = 50, name= "name", nullable = false)
    private String name;
    @Column(length = 50, name= "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "student_courses",
            joinColumns = @JoinColumn(name = "student_email"),
            inverseJoinColumns = @JoinColumn(name = "courses_id"))
    private List<Course> courses = new ArrayList<>();

    public Student() {
    }

    public Student(String email, String name, String password, List<Course> courses) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.courses = courses;
    }

    public Student(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(email, student.email) && Objects.equals(name, student.name) && Objects.equals(password, student.password) && Objects.equals(courses, student.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password, courses);
    }
    //Helper method to register student to a course and vice versa
    public void registerForCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.getStudents().add(this);
        }
    }
}
