package com.example.reservations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

        private Long id;
        private String nom;
        private String prenom;
        private String email;
        private String telephone;
        private LocalDateTime dateInscription;

       /* public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getNom() {
                return nom;
        }

        public void setNom(String nom) {
                this.nom = nom;
        }

        public String getPrenom() {
                return prenom;
        }

        public void setPrenom(String prenom) {
                this.prenom = prenom;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getTelephone() {
                return telephone;
        }

        public void setTelephone(String telephone) {
                this.telephone = telephone;
        }

        public LocalDateTime getDateInscription() {
                return dateInscription;
        }

        public void setDateInscription(LocalDateTime dateInscription) {
                this.dateInscription = dateInscription;
        }

        public ClientDto(Long id, String nom, String prenom, String email, String telephone, LocalDateTime dateInscription) {
                this.id = id;
                this.nom = nom;
                this.prenom = prenom;
                this.email = email;
                this.telephone = telephone;
                this.dateInscription = dateInscription;
        }

        public ClientDto() {
        }*/
}


