package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "students_tests", schema = "testing_system", catalog = "")
public class StudentsTests {
    private long id;
    private long studentId;
    private long testId;
    private BigDecimal result;
    private String computerName;
    private Timestamp dateAndTime;
    private Students studentsByStudentId;
    private Tests testsByTestId;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "student_id", nullable = false)
    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    @Basic
    @Column(name = "test_id", nullable = false)
    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
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
    @Column(name = "computer_name", nullable = false, length = 255)
    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    @Basic
    @Column(name = "date_and_time", nullable = false)
    public Timestamp getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Timestamp dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentsTests that = (StudentsTests) o;

        if (id != that.id) return false;
        if (studentId != that.studentId) return false;
        if (testId != that.testId) return false;
        if (result != null ? !result.equals(that.result) : that.result != null) return false;
        if (computerName != null ? !computerName.equals(that.computerName) : that.computerName != null) return false;
        if (dateAndTime != null ? !dateAndTime.equals(that.dateAndTime) : that.dateAndTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = (int) (id ^ (id >>> 32));
        result1 = 31 * result1 + (int) (studentId ^ (studentId >>> 32));
        result1 = 31 * result1 + (int) (testId ^ (testId >>> 32));
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (computerName != null ? computerName.hashCode() : 0);
        result1 = 31 * result1 + (dateAndTime != null ? dateAndTime.hashCode() : 0);
        return result1;
    }

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    public Students getStudentsByStudentId() {
        return studentsByStudentId;
    }

    public void setStudentsByStudentId(Students studentsByStudentId) {
        this.studentsByStudentId = studentsByStudentId;
    }

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id", nullable = false)
    public Tests getTestsByTestId() {
        return testsByTestId;
    }

    public void setTestsByTestId(Tests testsByTestId) {
        this.testsByTestId = testsByTestId;
    }
}
