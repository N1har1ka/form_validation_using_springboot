package com.example.student_registration.controller;

import com.example.student_registration.entity.Student;
import com.example.student_registration.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Show Registration Form
    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    // Create / Submit Form
    @PostMapping("/register")
    public String submitForm(@Valid @ModelAttribute("student") Student student,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (studentService.emailExists(student.getEmail())) {
            model.addAttribute("error", "Email is already registered!");
            return "register";
        }

        studentService.save(student);
        model.addAttribute("message", "Student Registered Successfully!");
        return "success";
    }

    // Read - View All Students
    @GetMapping("/students")
    public String viewAllStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "student_list"; // Create student_list.html to show all students
    }

    // Read - View Single Student by ID
    @GetMapping("/students/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            model.addAttribute("error", "Student not found");
            return "error";
        }
        model.addAttribute("student", student);
        return "view_student"; // Create view_student.html
    }

    // Update - Show Edit Form
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            model.addAttribute("error", "Student not found");
            return "error";
        }
        model.addAttribute("student", student);
        return "edit_student"; // Create edit_student.html
    }

    // Update - Save Edited Student
    @PostMapping("/students/update/{id}")
    public String updateStudent(@PathVariable Long id, @Valid @ModelAttribute("student") Student updatedStudent,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "edit_student";
        }

        Student student = studentService.updateStudent(id, updatedStudent);
        if (student == null) {
            model.addAttribute("error", "Update failed");
            return "error";
        }

        return "redirect:/students";
    }

    // Delete - By ID
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, Model model) {
        boolean deleted = studentService.deleteStudent(id);
        if (!deleted) {
            model.addAttribute("error", "Delete failed");
            return "error";
        }
        return "redirect:/students";
    }

    // Optional: Success Page
    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }
}
