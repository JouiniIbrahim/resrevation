package com.example.reservations.services.implementation;

import com.example.reservations.domain.Client;
import com.example.reservations.domain.Reservation;
import com.example.reservations.domain.StatutReservation;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.dto.ReservationResponseDto;
import com.example.reservations.dto.TaskApprovalDTO;
import com.example.reservations.dto.TaskDTO;
import com.example.reservations.mapper.ReservationMapper;
import com.example.reservations.repositories.ReservationRepo;
import com.example.reservations.repositories.StatutReservationRepo;
import com.example.reservations.services.ReservationService;
import jakarta.transaction.Transactional;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReservationSerImp implements ReservationService {

    @Autowired
    private ReservationRepo reservationRepo;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private StatutReservationRepo statutReservationRepo;
    @Autowired
    private  RuntimeService runtimeService;
    @Autowired
    private  TaskService taskService;

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
    public Reservation lancheProcess(Reservation reservation){
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1qriv8k", variables);
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
    public List<ReservationResponseDto> findAll() {
        // Retrieve all reservations and return them as DTOs
        return reservationRepo.allReservations();
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

    /**
     * Approve or disapprove a task based on the approval status provided in the DTO.
     *
     * @param taskApprovalDTO The DTO containing task details and approval status.
     * @return A response indicating the success or failure of the operation.
     */
    public ResponseEntity<String> approveOrDisapproveTask(TaskApprovalDTO taskApprovalDTO) {
        // Extract task details from DTO
        String taskId = taskApprovalDTO.getTaskId();
        String processInstanceId = taskApprovalDTO.getProcessInstanceId();
        Boolean approved = taskApprovalDTO.getApproved();

        System.out.println("Trying to approve/disapprove task with ID: " + taskId);

        // Query the task by taskId and processInstanceId
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .processInstanceId(processInstanceId)
                .singleResult();

        // Check if task is found
        if (task != null) {
            // Prepare task completion variables
            Map<String, Object> variables = new HashMap<>();
            variables.put("statut", approved);

            // Complete the task
            taskService.complete(taskId, variables);
            System.out.println("Task with Name " + task.getName() + " has been completed with status: " + approved);

            // Return a success message
            return ResponseEntity.status(HttpStatus.OK).body("Task with Name " + task.getName() + " has been successfully completed.");
        } else {
            // If task is not found, log and return an error message
            System.out.println("No task found with the given taskId: " + taskId + " and processInstanceId: " + processInstanceId);

            // Return a failure message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No task found with the given ID or process instance.");
        }
    }



    public void updateReservationStatus(Reservation res, Boolean statut) {
        // Find the reservation by client ID
        Reservation reservation = reservationRepo.findById(res.getId()).get();

        if (reservation!=null) {
            // Get the statut as a string
            String statutLibelle = statut != null && statut ? "CONFIRMÉE" : "ANNULÉE";

            // Find the StatutReservation by libelle (statut)
            StatutReservation statutReservation = statutReservationRepo.findByLibelle(statutLibelle)
                    .orElseThrow(() -> new IllegalArgumentException("Statut not found for libelle: " + statutLibelle));

            // Set the statut in the reservation
            reservation.setStatut(statutReservation);

            // Save the updated reservation back to the database
            reservationRepo.save(reservation);
        }
    }

}

