package com.academiadev.infrastructure.ui;

import com.academiadev.application.repositories.UserRepository;
import com.academiadev.application.usecases.*;
import com.academiadev.domain.entities.*;
import com.academiadev.domain.enums.DifficultyLevel;
import com.academiadev.domain.exceptions.BusinessException;
import com.academiadev.domain.exceptions.EnrollmentException;
import com.academiadev.infrastructure.utils.GenericCsvExporter;

import java.util.*;

public class ConsoleController {
	private final Scanner scanner;
    private final ConsoleView view;
    private final UserRepository userRepository;
    private final GenericCsvExporter csvExporter;
 
    private final EnrollStudentUseCase enrollStudentUseCase;
    private final CancelEnrollmentUseCase cancelEnrollmentUseCase;
    private final UpdateProgressUseCase updateProgressUseCase;
    private final OpenTicketUseCase openTicketUseCase;
    private final ProcessTicketUseCase processTicketUseCase;
    private final ChangeCourseStatusUseCase changeCourseStatusUseCase;
    private final ChangeStudentPlanUseCase changeStudentPlanUseCase;
    private final ReportUseCase reportUseCase;
 
    public ConsoleController(Scanner scanner,
                             ConsoleView view,
                             UserRepository userRepository,
                             GenericCsvExporter csvExporter,
                             EnrollStudentUseCase enrollStudentUseCase,
                             CancelEnrollmentUseCase cancelEnrollmentUseCase,
                             UpdateProgressUseCase updateProgressUseCase,
                             OpenTicketUseCase openTicketUseCase,
                             ProcessTicketUseCase processTicketUseCase,
                             ChangeCourseStatusUseCase changeCourseStatusUseCase,
                             ChangeStudentPlanUseCase changeStudentPlanUseCase,
                             ReportUseCase reportUseCase) {
        this.scanner = scanner;
        this.view = view;
        this.userRepository = userRepository;
        this.csvExporter = csvExporter;
        this.enrollStudentUseCase = enrollStudentUseCase;
        this.cancelEnrollmentUseCase = cancelEnrollmentUseCase;
        this.updateProgressUseCase = updateProgressUseCase;
        this.openTicketUseCase = openTicketUseCase;
        this.processTicketUseCase = processTicketUseCase;
        this.changeCourseStatusUseCase = changeCourseStatusUseCase;
        this.changeStudentPlanUseCase = changeStudentPlanUseCase;
        this.reportUseCase = reportUseCase;
    }

    
    public void run() {
        view.printTitle("Bem-vindo à AcademiaDev");
        boolean running = true;
        while (running) {
            User loggedUser = login();
            if (loggedUser == null) {
                view.printMessage("E-mail não encontrado. Tente novamente.");
                continue;
            }
            if (loggedUser instanceof Admin) {
                adminMenu((Admin) loggedUser);
            } else {
                studentMenu((Student) loggedUser);
            }
            view.printMessage("Deseja fazer login com outro usuário? (s/n)");
            running = scanner.nextLine().trim().equalsIgnoreCase("s");
        }
        view.printMessage("Até logo!");
    }
 

    private User login() {
        view.printSection("LOGIN");
        System.out.print("  Digite seu e-mail: ");
        String email = scanner.nextLine().trim();
        return userRepository.findByEmail(email).orElse(null);
    }
 

    private void adminMenu(Admin admin) {
        boolean inMenu = true;
        while (inMenu) {
            view.printMenu("MENU ADMINISTRADOR — " + admin.getName(), new String[]{
                    "Gerenciar status de cursos",
                    "Gerenciar plano de aluno",
                    "Atender ticket de suporte",
                    "Relatórios e análises",
                    "Exportar dados (CSV)",
                    "Consultar catálogo de cursos",
                    "Abrir ticket de suporte"
            });
            int opt = readInt();
            switch (opt) {
                case 1  -> handleCourseStatus();
                case 2  -> handleChangeStudentPlan();
                case 3  -> handleProcessTicket();
                case 4  -> handleReports();
                case 5  -> handleExportCsv();
                case 6  -> handleViewCatalog();
                case 7  -> handleOpenTicket(admin);
                case 0  -> inMenu = false;
                default -> view.printError("Opção inválida.");
            }
        }
    }
 

    private void studentMenu(Student student) {
        boolean inMenu = true;
        while (inMenu) {
            view.printMenu("MENU ALUNO — " + student.getName() +
                        " [Plano: " + student.getSubscriptionPlan() + "]", new String[]{
	                    "Matricular-se em curso",
	                    "Consultar minhas matrículas",
	                    "Atualizar progresso em curso",
	                    "Cancelar matrícula",
	                    "Consultar catálogo de cursos",
	                    "Abrir ticket de suporte"
            });
            int opt = readInt();
            switch (opt) {
                case 1 -> handleEnroll(student);
                case 2 -> handleViewEnrollments(student);
                case 3 -> handleUpdateProgress(student);
                case 4 -> handleCancelEnrollment(student);
                case 5 -> handleViewCatalog();
                case 6 -> handleOpenTicket(student);
                case 0 -> inMenu = false;
                default -> view.printError("Opção inválida.");
            }
        }
    }
 

    private void handleViewCatalog() {
        view.printSection("CATÁLOGO DE CURSOS ATIVOS");
        view.printActiveCourseList(reportUseCase.allCourses());
    }
 
    private void handleOpenTicket(User user) {
        view.printSection("ABRIR TICKET DE SUPORTE");
        System.out.print("  Título: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Mensagem: ");
        String message = scanner.nextLine().trim();
        try {
            SupportTicket ticket = openTicketUseCase.execute(user, title, message);
            view.printSuccess("Ticket #" + ticket.getId() + " aberto com sucesso!");
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }
 

    private void handleEnroll(Student student) {
        view.printSection("MATRICULAR-SE EM CURSO");
        handleViewCatalog();
        System.out.print("\n  Digite o título exato do curso: ");
        String title = scanner.nextLine().trim();
        try {
            Enrollment e = enrollStudentUseCase.execute(student, title);
            view.printSuccess("Matrícula realizada! Curso: " + e.getCourse().getTitle());
        } catch (EnrollmentException | BusinessException e) {
            view.printError(e.getMessage());
        }
    }
 
    private void handleViewEnrollments(Student student) {
        view.printSection("MINHAS MATRÍCULAS");
        List<Enrollment> enrollments = getEnrollmentsForStudent(student);
        view.printEnrollmentList(enrollments);
    }
 
    private void handleUpdateProgress(Student student) {
        view.printSection("ATUALIZAR PROGRESSO");
        handleViewEnrollments(student);
        System.out.print("\n  Título do curso: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Novo percentual (0-100): ");
        int percent = readInt();
        try {
            Enrollment e = updateProgressUseCase.execute(student, title, percent);
            view.printSuccess("Progresso atualizado: " + e.getProgress() +
                              "% em \"" + e.getCourse().getTitle() + "\"");
        } catch (BusinessException | EnrollmentException e) {
            view.printError(e.getMessage());
        }
    }
 
    private void handleCancelEnrollment(Student student) {
        view.printSection("CANCELAR MATRÍCULA");
        handleViewEnrollments(student);
        System.out.print("\n  Título do curso para cancelar: ");
        String title = scanner.nextLine().trim();
        try {
            cancelEnrollmentUseCase.execute(student, title);
            view.printSuccess("Matrícula em \"" + title + "\" cancelada.");
        } catch (BusinessException | EnrollmentException e) {
            view.printError(e.getMessage());
        }
    }
 

    private void handleCourseStatus() {
        view.printSection("GERENCIAR STATUS DE CURSOS");
        view.printCourseList(reportUseCase.allCourses());
        System.out.print("\n  Título do curso: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Ação (1-Ativar / 2-Inativar): ");
        int action = readInt();
        try {
            Course c = (action == 1)
                    ? changeCourseStatusUseCase.activate(title)
                    : changeCourseStatusUseCase.deactivate(title);
            view.printSuccess("Curso \"" + c.getTitle() + "\" agora está " + c.getStatus());
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }
 
    private void handleChangeStudentPlan() {
        view.printSection("ALTERAR PLANO DE ALUNO");
        view.printStudentsByPlan(reportUseCase.studentsByPlan());
        System.out.print("\n  E-mail do aluno: ");
        String email = scanner.nextLine().trim();
        System.out.print("  Novo plano (BASIC / PREMIUM): ");
        String plan = scanner.nextLine().trim();
        try {
            Student s = changeStudentPlanUseCase.execute(email, plan);
            view.printSuccess("Plano de " + s.getName() +
                              " alterado para " + s.getSubscriptionPlan());
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }
 
    private void handleProcessTicket() {
        view.printSection("ATENDER TICKET DE SUPORTE");
        view.printMessage("Tickets na fila: " + processTicketUseCase.pendingCount());
        try {
            SupportTicket ticket = processTicketUseCase.execute();
            view.printSuccess("Ticket processado:");
            view.printTicket(ticket);
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }
 
    private void handleReports() {
        boolean inMenu = true;
        while (inMenu) {
            view.printMenu("RELATÓRIOS", new String[]{
                    "Cursos por nível de dificuldade",
                    "Instrutores únicos de cursos ativos",
                    "Alunos agrupados por plano",
                    "Média geral de progresso",
                    "Aluno com mais matrículas"
            });
            int opt = readInt();
            switch (opt) {
                case 1 -> {
                    view.printSection("CURSOS POR NÍVEL");
                    System.out.print("  Nível (BEGINNER/INTERMEDIATE/ADVANCED): ");
                    String lvl = scanner.nextLine().trim().toUpperCase();
                    try {
                        DifficultyLevel level = DifficultyLevel.valueOf(lvl);
                        view.printCourseList(reportUseCase.coursesByLevel(level));
                    } catch (IllegalArgumentException e) {
                        view.printError("Nível inválido. Use BEGINNER, INTERMEDIATE ou ADVANCED.");
                    }
                }
                case 2 -> {
                    view.printSection("INSTRUTORES ÚNICOS (CURSOS ATIVOS)");
                    view.printInstructors(reportUseCase.activeInstructors());
                }
                case 3 -> {
                    view.printSection("ALUNOS POR PLANO");
                    view.printStudentsByPlan(reportUseCase.studentsByPlan());
                }
                case 4 -> {
                    view.printSection("MÉDIA GERAL DE PROGRESSO");
                    view.printMessage(String.format("%.1f%%", reportUseCase.averageProgress()));
                }
                case 5 -> {
                    view.printSection("ALUNO COM MAIS MATRÍCULAS");
                    reportUseCase.studentWithMostEnrollments()
                            .ifPresentOrElse(
                                s -> view.printMessage(s.getName() + " <" + s.getEmail() + ">"),
                                () -> view.printMessage("Nenhum aluno matriculado ainda.")
                            );
                }
                case 0  -> inMenu = false;
                default -> view.printError("Opção inválida.");
            }
        }
    }
 
    private void handleExportCsv() {
        view.printSection("EXPORTAR DADOS (CSV)");
        System.out.println("  O que exportar?");
        System.out.println("  [1] Cursos");
        System.out.println("  [2] Alunos");
        System.out.print("  Opção: ");
        int opt = readInt();
 
        List<?> data;
        List<String> availableFields;
 
        if (opt == 1) {
            data = reportUseCase.allCourses();
            availableFields = List.of("title", "instructorName",
                                      "durationInHours", "difficultyLevel", "status");
        } else if (opt == 2) {
            data = reportUseCase.allStudents();
            availableFields = List.of("name", "email", "subscriptionPlan");
        } else {
            view.printError("Opção inválida.");
            return;
        }
 
        System.out.println("\n  Campos disponíveis: " + availableFields);
        System.out.print("  Digite os campos separados por vírgula: ");
        String input = scanner.nextLine().trim();
 
        List<String> chosenFields = Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(f -> !f.isEmpty())
                .toList();
 
        String csv = csvExporter.export(data, chosenFields);
        view.printCsv(csv);
    }
 

    private List<Enrollment> getEnrollmentsForStudent(Student student) {
        return reportUseCase.allEnrollments().stream()
                .filter(e -> e.getStudent().getId().equals(student.getId()))
                .toList();
    }
 
    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
