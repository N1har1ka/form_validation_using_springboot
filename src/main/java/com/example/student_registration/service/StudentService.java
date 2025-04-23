package com.example.student_registration.service;

import com.example.student_registration.entity.Student;
import com.example.student_registration.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Save new student
    public void save(Student student) {
        studentRepository.save(student);
    }

    // Check if email already exists
    public boolean emailExists(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Student getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    // Update student
    public Student updateStudent(Long id, Student updatedStudent) {
        Optional<Student> existingStudentOpt = studentRepository.findById(id);
        if (existingStudentOpt.isPresent()) {
            Student existingStudent = existingStudentOpt.get();
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setEmail(updatedStudent.getEmail());
            existingStudent.setAge(updatedStudent.getAge());
            existingStudent.setCourse(updatedStudent.getCourse());
            return studentRepository.save(existingStudent);
        }
        return null;
    }

    // Delete student by ID
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
