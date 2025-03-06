package com.example.reservations.services.implementation;

import com.example.reservations.domain.Client;
import com.example.reservations.domain.Reservation;
import com.example.reservations.domain.StatutReservation;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.dto.ReservationResponseDto;
import com.example.reservations.dto.TaskApprovalDTO;
import com.example.reservations.mapper.ReservationMapper;
import com.example.reservations.repositories.ReservationRepo;
import com.example.reservations.repositories.StatutReservationRepo;
import com.example.reservations.services.ReservationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service("reservationSerImp")
public class ReservationSerImp implements ReservationService {


    private final ReservationRepo reservationRepo;

    private final ReservationMapper reservationMapper;

    private final StatutReservationRepo statutReservationRepo;

    private final RuntimeService runtimeService;

    private final TaskService taskService;
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Create a new reservation from the provided reservation DTO.
     *
     * @param reservationDto The reservation data transfer object containing reservation details.
     * @return The created reservation DTO.
     */
    @Override
    public ReservationDto create(ReservationDto reservationDto) {
        // Map the Reservation DTO to the Reservation entity
        Reservation reservation = reservationMapper.ToEntity(reservationDto);

        // Save the reservation to the database
        reservation = reservationRepo.save(reservation);

        reservation = lancheProcess(reservation);
        // Map the saved entity back to a DTO and return it
        return reservationMapper.ToDTO(reservation);
    }

    public Reservation lancheProcess(Reservation reservation) {
        System.out.println("************************************************");

        // Preparing variables for the Camunda process
        Map<String, Object> variables = new HashMap<>();
        variables.put("startedBy", reservation.getClient().getId());
        variables.put("client_id", reservation.getClient().getId());
        variables.put("reservation", reservation);
        variables.put("startDate", reservation.getDateDebut());
        variables.put("endDate", reservation.getDateFin());
        variables.put("type", "Reservation");

        // Start the process instance
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("reservation-diagramV2", variables);
        reservation.setProcessInstId(processInstance.getProcessInstanceId());
        reservation = reservationRepo.save(reservation);
        return reservation;
    }

    /**
     * Get all reservations from the repository.
     *
     * @return A list of all reservation response DTOs.
     */
    @Override
    public Page<ReservationResponseDto> findAll(Pageable pageable) {
        // Fetch paginated reservations from the repository
        Page<Reservation> reservationPage = reservationRepo.findAll(pageable);

        // Map Reservation entities to ReservationResponseDto
        return reservationPage.map(reservation -> {
            ReservationResponseDto dto = new ReservationResponseDto();
            dto.setId(reservation.getId());
            dto.setDateDebut(reservation.getDateDebut());
            dto.setDateFin(reservation.getDateFin());
            dto.setPrixTotal(reservation.getPrixTotal());
            dto.setStatutId(reservation.getStatut().getId());
            dto.setClientid(reservation.getClient().getId());
            dto.setMessageFr(reservation.getMessageFr());
            return dto;
        });
    }

    /**
     * Find reservations that have a specific statut ID and fall within a given date period.
     *
     * @param statutId  The ID of the reservation status to filter by.
     * @param startDate The start date of the period to filter reservations.
     * @param endDate   The end date of the period to filter reservations.
     * @return A list of reservation response DTOs matching the filters.
     */
    @Override
    public List<ReservationResponseDto> findReservations(Long statutId, LocalDate startDate, LocalDate endDate) {
        // Query the repository to get reservations by statut and period
        return reservationRepo.findReservationsByStatutAndPeriod(statutId, startDate, endDate);
    }

    /**
     * Calculate the total revenue generated by reservations with a specific statut ID within a given date period.
     *
     * @param statutId  The statut ID of the reservations to calculate revenue for.
     * @param startDate The start date of the period to calculate revenue.
     * @param endDate   The end date of the period to calculate revenue.
     * @return The total revenue of the filtered reservations.
     */
    @Override
    public Double TotalRevenue(Long statutId, LocalDate startDate, LocalDate endDate) {
        // Calculate and return the total revenue by querying the repository
        return reservationRepo.TotalRevenue(statutId, startDate, endDate);
    }

    /**
     * Find clients who have made more than X reservations in a specific year.
     *
     * @param year The year to filter the reservations by.
     * @param x    The minimum number of reservations a client should have made.
     * @return A list of clients who meet the reservation criteria.
     */
    @Override
    public List<Client> ReservationsMore(int year, int x) {
        // Query the repository to get clients with more than X reservations in the given year
        return reservationRepo.ReservationsMore(year, x);
    }

//    /**
//     * Approve or disapprove a task based on the approval status provided in the DTO.
//     *
//     * @param taskApprovalDTO The DTO containing task details and approval status.
//     * @return A response indicating the success or failure of the operation.
//     */
//    public ResponseEntity<String> approveOrDisapproveTask(TaskApprovalDTO taskApprovalDTO) {
//        // Extract task details from DTO
//        String taskId = taskApprovalDTO.getTaskId();
//        String processInstanceId = taskApprovalDTO.getProcessInstanceId();
//        Boolean approved = taskApprovalDTO.getApproved();
//
//        System.out.println("Trying to approve/disapprove task with ID: " + taskId);
//
//        // Query the task by taskId and processInstanceId
//        Task task = taskService.createTaskQuery()
//                .taskId(taskId)
//                .processInstanceId(processInstanceId)
//                .singleResult();
//
//        // Check if task is found
//        if (task != null) {
//            // Prepare task completion variables
//            Map<String, Object> variables = new HashMap<>();
//            variables.put("statut", approved);
//
//            // Complete the task
//            taskService.complete(taskId, variables);
//            System.out.println("Task with Name " + task.getName() + " has been completed with status: " + approved);
//
//            // Return a success message
//            return ResponseEntity.status(HttpStatus.OK).body("Task with Name " + task.getName() + " has been successfully completed.");
//        } else {
//            // If task is not found, log and return an error message
//            System.out.println("No task found with the given taskId: " + taskId + " and processInstanceId: " + processInstanceId);
//
//            // Return a failure message
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No task found with the given ID or process instance.");
//        }
//    }


    public void updateReservationStatus(Reservation res, Boolean statut, String decisionSource) {
        Reservation reservation = reservationRepo.findById(res.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + res.getId()));

        String statutLibelle;
        if ("hotel".equals(decisionSource)) {
            statutLibelle = (statut != null && statut) ? "CONFIRMÉE" : "ANNULÉE";
        } else if ("agency".equals(decisionSource) && statut == false) {
            statutLibelle = "ANNULÉE";
        } else {
            return;
        }

        StatutReservation statutReservation = statutReservationRepo.findByLibelle(statutLibelle)
                .orElseThrow(() -> new IllegalArgumentException("Statut not found for libelle: " + statutLibelle));

        reservation.setStatut(statutReservation);
        reservationRepo.save(reservation);
    }

    public ResponseEntity<String> approveOrDisapproveTask(TaskApprovalDTO taskApprovalDTO) {
        String taskId = taskApprovalDTO.getTaskId();
        String processInstanceId = taskApprovalDTO.getProcessInstanceId();
        Boolean approved = taskApprovalDTO.getApproved();
        Reservation reservation = (Reservation) taskService.getVariable(taskId, "reservation");

        System.out.println("Processing task with ID: " + taskId);

        // Query the task
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .processInstanceId(processInstanceId)
                .singleResult();

        if (task == null) {
            System.out.println("No task found with taskId: " + taskId + " and processInstanceId: " + processInstanceId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No task found with the given ID or process instance.");
        }

        String decisionSource = task.getName().equals("Agence manager") ? "agency" : "hotel";

        // Prepare variables for task completion
        Map<String, Object> variables = new HashMap<>();
        variables.put("statut", approved);
        variables.put("decisionSource", decisionSource);
        variables.put("reservation", reservation);

        // Update reservation status
        Reservation res = reservationRepo.findById(reservation.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservation.getId()));
        updateReservationStatus(res, approved, decisionSource);

        // Complete the current task (Agence manager or Hotel manager)
        taskService.complete(taskId, variables);
        System.out.println("Task '" + task.getName() + "' completed with status: " + approved);

        // If this is the Hotel manager's task, handle the send tasks
        if (decisionSource.equals("hotel")) {
            // Find and complete the appropriate send task
            Task sendTask = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskDefinitionKey(approved ? "Activity_09bqe0e" : "Activity_1fmhm53") // IDs from BPMN
                    .singleResult();

            if (sendTask != null) {
                sendEmail(reservation, approved); // Send email based on approval status
                taskService.complete(sendTask.getId()); // Complete the send task
                System.out.println("Send task '" + sendTask.getName() + "' completed.");
            } else {
                System.out.println("Send task not found for process instance: " + processInstanceId);
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Task '" + task.getName() + "' has been successfully completed.");
    }

    public void sendEmail(Reservation reservation, Boolean statut) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(reservation.getClient().getEmail());
            helper.setSubject(statut ? "Reservation Confirmed" : "Reservation Rejected");
            helper.setFrom("no-reply@siga.com");

            String emailBody = statut
                    ? "Dear" +reservation.getClient().getNom()+",\n\nYour reservation (ID: " + reservation.getId() + ") has been confirmed.\nThank you for choosing us!\n\nBest regards,\nSIGA"
                    : "Dear "+reservation.getClient().getNom()+",\n\nWe regret to inform you that your reservation (ID: " + reservation.getId() + ") has been rejected.\nFor further assistance, please contact us.\n\nBest regards,\nSIGA ";
            helper.setText(emailBody);

            mailSender.send(message);
            System.out.println("Email sent to " + reservation.getClient().getEmail() + " with status: " + (statut ? "Confirmed" : "Rejected"));
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }


}

