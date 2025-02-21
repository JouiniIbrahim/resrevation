package com.example.reservations.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskApprovalDTO {

    private String taskId;
    private String processInstanceId;
    private Boolean approved;
}
