package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ordination.*;
import java.time.LocalDate;
import java.time.LocalTime;


public class DagligSkaevTest {


    @Test
    void testOpretDosisgyldig() {
        // Arrange
        Laegemiddel lm = new Laegemiddel("Paracetamol", 0.1, 0.15, 0.16, "Styk");
        DagligSkaev ds = new DagligSkaev(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 10),
                lm);
        // Act
        ds.opretDosis(LocalTime.of(8, 0), 2.0);




        // Assert
        assertEquals(1, ds.getDoser().size());


    }


    @Test
    void testSamletDosis() {
        // Arrange
        Laegemiddel lm = new Laegemiddel("Paracetamol", 0.1, 0.15, 0.16, "Styk");
        DagligSkaev ds = new DagligSkaev(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 10),
                lm);




        // Act
        ds.opretDosis(LocalTime.of(8, 0), 2.0);
        ds.opretDosis(LocalTime.of(12, 0), 2.0);




        // Assert
        assertEquals(40.0, ds.samletDosis());




    }


    @Test
    void testDoegnDosis() {
        // Arrange
        Laegemiddel lm = new Laegemiddel("Paracetamol", 0.1, 0.15, 0.16, "Styk");
        DagligSkaev ds = new DagligSkaev(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 10),
                lm);




        // Act
        ds.opretDosis(LocalTime.of(8, 0), 2.0);
        ds.opretDosis(LocalTime.of(12, 0), 1.0);
        ds.opretDosis(LocalTime.of(18, 0), 1.5);




        // Assert
        assertEquals(4.5, ds.doegnDosis());






    }
}
