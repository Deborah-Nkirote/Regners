package Reigners.demo.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@Table(name="Sermon")
@NoArgsConstructor
@AllArgsConstructor
public class Sermon {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String title;
    private String date;
    private String videoUrl;
    private String notes;
    private String downloadUrl;

        // Getters and setters
    }
