package com.example.reservations.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDTO {

    private String id;
    private String name;
    private String assignee;
    private String owner;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private String priority;
    private String dueDate;
    private String createTime;

}
