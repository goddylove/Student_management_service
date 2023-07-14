package sba.sms.models;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name= "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 50, name= "id", nullable = false)
    private int id;
    @Column(length = 50, name= "name", nullable = false, unique= true)
    private String name;
    @Column(length = 50, name= "instructor", nullable = false)
    private String instructor;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "courses", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Student> students = new ArrayList<>();


    public Course() {
    }

    public Course(int id, String name, String instructor, List<Student> students) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
        this.students = students;
    }

    public Course(int id, String name, String instructor) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
    }

    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", instructor='" + instructor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return id == course.id && Objects.equals(name, course.name) && Objects.equals(instructor, course.instructor) && Objects.equals(students, course.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, instructor, students);
    }
    public void addStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
            student.getCourses().add(this);
        }
    }

    public void removeStudent(Student student) {
        if (student != null && students.contains(student)) {
            students.remove(student);
            student.getCourses().remove(this);
        }
    }
}
