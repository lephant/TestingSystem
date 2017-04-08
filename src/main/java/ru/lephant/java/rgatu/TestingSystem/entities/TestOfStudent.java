package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "students_tests")
public class TestOfStudent implements Serializable {

    private long id;
    private Student student;
    private Test test;
    private BigDecimal result;
    private int mark;
    private String computerName;
    private Date dateAndTime;


    public TestOfStudent() {
    }

    public TestOfStudent(Student student, Test test) {
        this.student = student;
        this.test = test;
    }

    public TestOfStudent(Student student, Test test, BigDecimal result) {
        this.student = student;
        this.test = test;
        this.result = result;
    }

    public TestOfStudent(Student student, Test test, BigDecimal result, Date dateAndTime) {
        this.student = student;
        this.test = test;
        this.result = result;
        this.dateAndTime = dateAndTime;
    }

    public TestOfStudent(Student student, Test test, BigDecimal result, String computerName) {
        this.student = student;
        this.test = test;
        this.result = result;
        this.computerName = computerName;
    }

    public TestOfStudent(Student student, Test test, BigDecimal result, String computerName, Date dateAndTime) {
        this.student = student;
        this.test = test;
        this.result = result;
        this.computerName = computerName;
        this.dateAndTime = dateAndTime;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id", nullable = false)
    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @Basic
    @Column(name = "result", nullable = false, precision = 4)
    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    @Basic
    @Column(name = "mark", nullable = false)
    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    @Basic
    @Column(name = "computer_name", nullable = false, length = 255)
    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_and_time", nullable = false)
    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestOfStudent that = (TestOfStudent) o;

        if (id != that.id) return false;
        if (mark != that.mark) return false;
        if (!student.equals(that.student)) return false;
        if (!test.equals(that.test)) return false;
        if (!result.equals(that.result)) return false;
        if (!computerName.equals(that.computerName)) return false;
        return dateAndTime.equals(that.dateAndTime);
    }

    @Override
    public int hashCode() {
        int result1 = (int) (id ^ (id >>> 32));
        result1 = 31 * result1 + student.hashCode();
        result1 = 31 * result1 + test.hashCode();
        result1 = 31 * result1 + result.hashCode();
        result1 = 31 * result1 + mark;
        result1 = 31 * result1 + computerName.hashCode();
        result1 = 31 * result1 + dateAndTime.hashCode();
        return result1;
    }
}