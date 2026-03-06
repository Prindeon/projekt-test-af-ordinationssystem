package tests;

import ordination.DagligFast;
import ordination.Dosis;
import ordination.Laegemiddel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class DagligFastTest {


    // DagligFast constructor test
    @Test
    public void TC1_skabObjekt() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1, 1, 1, "Styk");

        // Opret objekt
        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Forventet resultat
        assertNotNull(dagligFast);
    }

    // Ugyldige cases
    @Test
    public void TC2_startDatoNull() {

        // Input
        LocalDate startDato = null;
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        // Forventet resultat
        assertThrows(IllegalArgumentException.class, () -> {
            new DagligFast(startDato, slutDato, lm);
        });
    }

    @Test
    public void TC2_slutDatoNull() {

        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = null;
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        // Forventet resultat
        assertThrows(IllegalArgumentException.class, () -> {
            new DagligFast(startDato, slutDato, lm);
        });
    }

    @Test
    public void TC3_laegemiddelNull() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = null;

        // Forventet resultat
        assertThrows(IllegalArgumentException.class, () -> {
            new DagligFast(startDato, slutDato, lm);
        });
    }

    @Test
    public void TC4_slutFoerStart() {

        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 1, 15);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        // Forventet resultat
        assertThrows(IllegalArgumentException.class, () -> {
            new DagligFast(startDato, slutDato, lm);
        });
    }

    // DagligFast dosis[] getDoser()
    @Test
    public void TC1_alleDoserFastsat() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Opret doser
        Dosis morgen = new Dosis(LocalTime.of(8,0), 1);
        Dosis middag = new Dosis(LocalTime.of(12,0), 1);
        Dosis aften = new Dosis(LocalTime.of(18,0), 1);
        Dosis nat = new Dosis(LocalTime.of(23,0), 1);

        // Sæt doser
        dagligFast.setDosis(0, morgen);
        dagligFast.setDosis(1, middag);
        dagligFast.setDosis(2, aften);
        dagligFast.setDosis(3, nat);

        // Forventet resultat
        assertEquals(4, dagligFast.getDoser().length);
    }

    @Test
    public void TC2_nogleDoserFastsat() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Opret doser
        Dosis morgen = new Dosis(LocalTime.of(8,0), 1);
        Dosis aften = new Dosis(LocalTime.of(18,0), 1);

        // Sæt kun nogle doser
        dagligFast.setDosis(0, morgen);
        dagligFast.setDosis(2, aften);

        // Forventet resultat
        assertNotNull(dagligFast.getDosis(0));
        assertNull(dagligFast.getDosis(1));
        assertNotNull(dagligFast.getDosis(2));
        assertNull(dagligFast.getDosis(3));
    }

    @Test
    public void TC3_ingenDoserFastsat() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Forventet resultat
        assertNull(dagligFast.getDosis(0));
        assertNull(dagligFast.getDosis(1));
        assertNull(dagligFast.getDosis(2));
        assertNull(dagligFast.getDosis(3));
    }

    // DagligFast setDosis(int index, Dosis dosis)
    @Test
    public void TC1_morgenDosis() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        int index = 0; // Morgen
        Dosis morgen = new Dosis(LocalTime.of(8, 0), 1.0);

        // Sæt dosis
        dagligFast.setDosis(index, morgen);

        // Forventet resultat
        assertEquals(morgen, dagligFast.getDosis(index));
    }

    @Test
    public void TC2_middagDosis() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        int index = 1; // Middag
        Dosis middag = new Dosis(LocalTime.of(12, 0), 1.0);

        // Sæt dosis
        dagligFast.setDosis(index, middag);

        // Forventet resultat
        assertEquals(middag, dagligFast.getDosis(index));
    }

    @Test
    public void TC3_aftenDosis() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        int index = 1; // Middag
        Dosis aften = new Dosis(LocalTime.of(18, 0), 1.0);

        // Sæt dosis
        dagligFast.setDosis(index, aften);

        // Forventet resultat
        assertEquals(aften, dagligFast.getDosis(index));
    }

    @Test
    public void TC4_natDosis() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        int index = 1; // Middag
        Dosis nat = new Dosis(LocalTime.of(23, 0), 1.0);

        // Sæt dosis
        dagligFast.setDosis(index, nat);

        // Forventet resultat
        assertEquals(nat, dagligFast.getDosis(index));
    }

    @Test
    public void TC5_indexForLav() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        int index = -1; // For lavt index
        Dosis dosis = new Dosis(LocalTime.of(8, 0), 1.0);

        // Forventet resultat
        assertThrows(IllegalArgumentException.class, () -> {
            dagligFast.setDosis(index, dosis);
        });
    }

    @Test
    public void TC6_indexForHoej() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        int index = 4; // For højt index
        Dosis dosis = new Dosis(LocalTime.of(8, 0), 1.0);

        // Forventet resultat
        assertThrows(IllegalArgumentException.class, () -> {
            dagligFast.setDosis(index, dosis);
        });
    }

    // DagligFast public double doegnDosis()
    @Test
    public void TC1_doegnDosisAlleDoser() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Opret doser med antal
        Dosis morgen = new Dosis(LocalTime.of(8, 0), 1.0);
        Dosis middag = new Dosis(LocalTime.of(12, 0), 1.0);
        Dosis aften = new Dosis(LocalTime.of(18, 0), 1.0);
        Dosis nat = new Dosis(LocalTime.of(23, 0), 2.0);

        // Sæt doser
        dagligFast.setDosis(0, morgen);
        dagligFast.setDosis(1, middag);
        dagligFast.setDosis(2, aften);
        dagligFast.setDosis(3, nat);

        // Forventet resultat
        assertEquals(5.0, dagligFast.doegnDosis(), 0.0001);
    }

    @Test
    public void TC2_nogleDoserErNull() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Opret doser
        Dosis morgen = new Dosis(LocalTime.of(8, 0), 1.0);
        Dosis aften = new Dosis(LocalTime.of(18, 0), 2.0);

        // Sæt kun nogle doser
        dagligFast.setDosis(0, morgen); // Morgen
        dagligFast.setDosis(2, aften);  // Aften

        // Forventet resultat
        assertEquals(3.0, dagligFast.doegnDosis(), 0.0001);
    }


    @Test
    public void TC3_alleDoserErNull() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Forventet resultat: doegnDosis() = 0.0
        assertEquals(0.0, dagligFast.doegnDosis(), 0.0001);
    }


    @Test
    public void TC4_alleDoserEr0() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Opret doser med antal
        Dosis morgen = new Dosis(LocalTime.of(8, 0), 0);
        Dosis middag = new Dosis(LocalTime.of(12, 0), 0);
        Dosis aften = new Dosis(LocalTime.of(18, 0), 0);
        Dosis nat = new Dosis(LocalTime.of(23, 0), 0);

        // Sæt doser
        dagligFast.setDosis(0, morgen);
        dagligFast.setDosis(1, middag);
        dagligFast.setDosis(2, aften);
        dagligFast.setDosis(3, nat);

        // Forventet resultat
        assertEquals(0, dagligFast.doegnDosis(), 0.0001);
    }

    // DagligFast public double samletDosis()
    @Test
    public void TC1_alleDoserSat() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Opret doser med antal
        Dosis morgen = new Dosis(LocalTime.of(8, 0), 4);
        Dosis middag = new Dosis(LocalTime.of(12, 0), 4);
        Dosis aften = new Dosis(LocalTime.of(18, 0), 4);
        Dosis nat = new Dosis(LocalTime.of(23, 0), 3);

        // Sæt doser
        dagligFast.setDosis(0, morgen);
        dagligFast.setDosis(1, middag);
        dagligFast.setDosis(2, aften);
        dagligFast.setDosis(3, nat);

        // Forventet resultat
        assertEquals(15, dagligFast.doegnDosis(), 0.0001);
    }

    @Test
    public void TC2_alleDoserNull() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Forventet resultat
        assertEquals(0.0, dagligFast.samletDosis(), 0.0001);
    }

    @Test
    public void TC3_alleDoser0() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 22);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Opret doser med antal
        Dosis morgen = new Dosis(LocalTime.of(8, 0), 0);
        Dosis middag = new Dosis(LocalTime.of(12, 0), 0);
        Dosis aften = new Dosis(LocalTime.of(18, 0), 0);
        Dosis nat = new Dosis(LocalTime.of(23, 0), 0);

        // Sæt doser
        dagligFast.setDosis(0, morgen);
        dagligFast.setDosis(1, middag);
        dagligFast.setDosis(2, aften);
        dagligFast.setDosis(3, nat);

        // Forventet resultat:
        assertEquals(0.0, dagligFast.samletDosis(), 0.0001);
    }

    @Test
    public void TC4_startErLigSlut() {
        // Input
        LocalDate startDato = LocalDate.of(2026, 2, 20);
        LocalDate slutDato = LocalDate.of(2026, 2, 20);
        Laegemiddel lm = new Laegemiddel("Panodil", 1.0, 1.0, 1.0, "mg");

        DagligFast dagligFast = new DagligFast(startDato, slutDato, lm);

        // Opret doser
        Dosis morgen = new Dosis(LocalTime.of(8, 0), 1.0);
        Dosis middag = new Dosis(LocalTime.of(12, 0), 1.0);
        Dosis aften = new Dosis(LocalTime.of(18, 0), 1.0);
        Dosis nat = new Dosis(LocalTime.of(23, 0), 2.0);

        // Sæt doser
        dagligFast.setDosis(0, morgen);
        dagligFast.setDosis(1, middag);
        dagligFast.setDosis(2, aften);
        dagligFast.setDosis(3, nat);

        // Forventet resultat
        assertEquals(5.0, dagligFast.samletDosis(), 0.0001);
    }
}
