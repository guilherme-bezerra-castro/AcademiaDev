package com.academiadev.main;

import com.academiadev.application.repositories.*;
import com.academiadev.application.usecases.*;
import com.academiadev.infrastructure.persistence.*;
import com.academiadev.infrastructure.ui.ConsoleController;
import com.academiadev.infrastructure.ui.ConsoleView;
import com.academiadev.infrastructure.utils.GenericCsvExporter;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
        System.out.println("AcademiaDev — Iniciando...\n");
 
        // Infrestrutura
        CourseRepository courseRepo = new CourseRepositoryInMemory();
        UserRepository userRepo = new UserRepositoryInMemory();
        EnrollmentRepository enrollRepo = new EnrollmentRepositoryInMemory();
        SupportTicketQueue ticketQueue = new SupportTicketQueueInMemory();
 
        // Dados
        InitialData.populate(courseRepo, userRepo);
 
        // Use Cases
        EnrollStudentUseCase enrollUseCase = new EnrollStudentUseCase(courseRepo, enrollRepo);
        CancelEnrollmentUseCase cancelUseCase = new CancelEnrollmentUseCase(courseRepo, enrollRepo);
        UpdateProgressUseCase progressUseCase = new UpdateProgressUseCase(courseRepo, enrollRepo);
        OpenTicketUseCase openTicket = new OpenTicketUseCase(ticketQueue);
        ProcessTicketUseCase processTicket = new ProcessTicketUseCase(ticketQueue);
        ChangeCourseStatusUseCase courseStatus = new ChangeCourseStatusUseCase(courseRepo);
        ChangeStudentPlanUseCase studentPlan = new ChangeStudentPlanUseCase(userRepo);
        ReportUseCase reportUseCase = new ReportUseCase(courseRepo, enrollRepo, userRepo);
 
        // UI
        Scanner scanner = new Scanner(System.in);
        ConsoleView view = new ConsoleView();
        GenericCsvExporter csvExporter = new GenericCsvExporter();
 
        ConsoleController controller = new ConsoleController(
                scanner, view, userRepo, csvExporter,
                enrollUseCase, cancelUseCase, progressUseCase,
                openTicket, processTicket, courseStatus,
                studentPlan, reportUseCase
        );
 
        controller.run();
 
        scanner.close();
    }
}
