package com.example.reservations.controllers;

import com.example.reservations.domain.Client;
import com.example.reservations.dto.*;
import com.example.reservations.services.implementation.ReservationSerImp;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationSerImp reservationSerImp;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private  TaskService taskService;

    /**
     * API to save a new reservation.
     * This endpoint accepts a ReservationDto in the request body and saves it in the system.
     * If the statut (status) field is missing, it returns a 400 Bad Request response.
     * If the reservation is successfully saved, it returns a 201 Created response.
     * In case of errors, it returns a 500 Internal Server Error response.
     */
    @PostMapping("/save")
    public ResponseEntity<ReservationDto> save(@RequestBody ReservationDto reservationDto) {
        if (reservationDto.getStatutId() == null) {
            reservationDto.setMessageFr("Le statut est requis.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reservationDto); // 400 if statut is missing
        }

        try {
            ReservationDto savedReservation = reservationSerImp.create(reservationDto);
            savedReservation.setMessageFr("Réservation enregistrée");

            // Return the created reservation with associated task (if any)
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation); // 201 if successful

        } catch (Exception e) {

            reservationDto.setMessageFr("Erreur lors de la création de la réservation.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(reservationDto); // 500 if failure
        }
    }


    /**
     * API to get all reservations.
     * This endpoint fetches all reservations from the system.
     * If no reservations are found, it returns a 204 No Content response.
     * If reservations are found, it returns a 200 OK response with the list of reservations.
     */
    @GetMapping("/all")
    public ResponseEntity<List<ReservationResponseDto>> allReservations() {
        List<ReservationResponseDto> reservations = reservationSerImp.findAll();
        if (reservations.isEmpty()) {
            ReservationResponseDto responseDto = new ReservationResponseDto();
            responseDto.setMessageFr("Aucune réservation trouvée.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(reservations); // 204 if no reservations
        }

        return ResponseEntity.status(HttpStatus.OK).body(reservations); // 200 if reservations found
    }

    /**
     * API to find reservations by status and period.
     * This endpoint allows filtering reservations based on their status and a specific period.
     * It takes a ReservationDto in the request body with statutId, dateDebut, and dateFin.
     * If no reservations are found, it returns a 204 No Content response.
     * If reservations are found, it returns a 200 OK response with the list of reservations.
     */
    @GetMapping("/statut-period")
    public ResponseEntity<List<ReservationResponseDto>> findReservationsByStatutAndPeriod(@RequestBody ReservationDto reservationDto) {
        List<ReservationResponseDto> reservations = reservationSerImp.findReservations(
                reservationDto.getStatutId(),
                reservationDto.getDateDebut(),
                reservationDto.getDateFin());

        if (reservations.isEmpty()) {
            ReservationResponseDto responseDto = new ReservationResponseDto();
            responseDto.setMessageFr("Aucune réservation trouvée pour la période et le statut spécifiés.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(reservations); // 204 if no reservations found
        }

        return ResponseEntity.status(HttpStatus.OK).body(reservations); // 200 if reservations found
    }

    /**
     * API to calculate total revenue by status and period.
     * This endpoint calculates the total revenue from reservations based on their status and a specified period.
     * It takes a ReservationDto in the request body with statutId, dateDebut, and dateFin.
     * If no revenue is found, it returns a 204 No Content response.
     * If revenue is calculated successfully, it returns a 200 OK response with the total revenue.
     */
    @GetMapping("/total")
    public ResponseEntity<ReservationDto> calculateTotalRevenueByStatutAndPeriod(@RequestBody ReservationDto reservationDto) {
        Double totalRevenue = reservationSerImp.TotalRevenue(
                reservationDto.getStatutId(),
                reservationDto.getDateDebut(),
                reservationDto.getDateFin());

        if (totalRevenue == null || totalRevenue == 0) {
            reservationDto.setMessageFr("Aucun revenu trouvé pour la période et le statut spécifiés.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(reservationDto); // 204 if no revenue
        }

        reservationDto.setMessageFr("Revenu total calculé avec succès.");
        reservationDto.setTotalRevenue(totalRevenue);
        return ResponseEntity.status(HttpStatus.OK).body(reservationDto); // 200 if total revenue is calculated
    }

    /**
     * API to find clients with more than a specified number of reservations.
     * This endpoint retrieves clients who have made more than a specified number of reservations in a given year.
     * It accepts the year and the minimum number of reservations (x) as query parameters.
     * If no clients are found, it returns a 204 No Content response.
     * If clients are found, it returns a 200 OK response with the list of clients.
     */
    @GetMapping("/reservations-more")
    public ResponseEntity<List<Client>> ReservationsMore(
            @RequestParam int year,
            @RequestParam int x) {
        List<Client> clients = reservationSerImp.ReservationsMore(year, x);
        if (clients.isEmpty()) {
            ReservationResponseDto responseDto = new ReservationResponseDto();
            responseDto.setMessageFr("Aucun client trouvé avec plus que le nombre spécifié de réservations.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(clients); // 204 if no clients found
        }
        return ResponseEntity.status(HttpStatus.OK).body(clients); // 200 if clients found
    }

    /**
     * API to approve or disapprove a task by the agency manager.
     * This endpoint allows an agency manager to approve or disapprove a task by providing the task details in a TaskApprovalDTO.
     * The task is then processed in the backend and an appropriate response is returned.
     */
    @PostMapping("/agence_manager")
    public ResponseEntity<String> agenceManager(@RequestBody TaskApprovalDTO taskApprovalDTO) {
        // Call the service method to approve or disapprove the task and get the TaskDTO
        return reservationSerImp.approveOrDisapproveTask(taskApprovalDTO);
    }

    /**
     * API to get tasks based on the process definition key.
     * This endpoint retrieves a list of tasks associated with a specific process definition key.
     * It returns the tasks as a list of TaskDTO objects, providing details like task ID, name, assignee, and priority.
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByProcessDefinitionKey() {
        // Hardcoded process definition key
        String processDefinitionKey = "Process_1qriv8k";  // Process definition key

        // Retrieve tasks by process definition key
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();

        // Convert Task entities to TaskDTOs
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getName(),
                        task.getAssignee(),
                        task.getOwner(),
                        task.getProcessInstanceId(),
                        task.getProcessDefinitionId(),
                        task.getTaskDefinitionKey(),
                        String.valueOf(task.getPriority()),
                        task.getDueDate() != null ? task.getDueDate().toString() : null,
                        task.getCreateTime() != null ? task.getCreateTime().toString() : null))
                .collect(Collectors.toList());

        // Return the task details as a response
        return ResponseEntity.status(HttpStatus.OK).body(taskDTOs);
    }

    /**
     * API to get completed tasks from the historical table.
     * This endpoint retrieves all tasks that have been marked as completed.
     * It returns the completed tasks as a list of TaskDTO objects.
     */
    @GetMapping("/completed-tasks")
    public ResponseEntity<List<TaskDTO>> getCompletedTasks() {

        // Retrieve all completed tasks from the historical table
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .finished()  // Ensure only completed tasks are included
                .list();

        // Convert HistoricTaskInstance entities to TaskDTOs
        List<TaskDTO> taskDTOs = historicTasks.stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getName(),
                        task.getAssignee(),
                        task.getOwner(),
                        task.getProcessInstanceId(),
                        task.getProcessDefinitionId(),
                        task.getTaskDefinitionKey(),
                        String.valueOf(task.getPriority()),
                        task.getDueDate() != null ? task.getDueDate().toString() : null,
                        task.getEndTime() != null ? task.getEndTime().toString() : null
                ))
                .collect(Collectors.toList());

        // Return the completed task details as a response
        return ResponseEntity.status(HttpStatus.OK).body(taskDTOs);
    }
    @GetMapping("/completed-tasks-name")
    public ResponseEntity<List<TaskDTO>> getCompletedTasksByName(@RequestParam String taskName) {

        // Retrieve all completed tasks from the historical table with a specific name
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .finished()  // Ensure only completed tasks are included
                .taskName(taskName)  // Filter tasks by name
                .list();

        // Convert HistoricTaskInstance entities to TaskDTOs
        List<TaskDTO> taskDTOs = historicTasks.stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getName(),
                        task.getAssignee(),
                        task.getOwner(),
                        task.getProcessInstanceId(),
                        task.getProcessDefinitionId(),
                        task.getTaskDefinitionKey(),
                        String.valueOf(task.getPriority()),
                        task.getDueDate() != null ? task.getDueDate().toString() : null,
                        task.getEndTime() != null ? task.getEndTime().toString() : null
                ))
                .collect(Collectors.toList());

        // Return the completed task details as a response
        return ResponseEntity.status(HttpStatus.OK).body(taskDTOs);
    }

}
