package tests;


import com.sun.source.tree.AssertTree;
import controller.Controller;
import ordination.Laegemiddel;
import ordination.Ordination;
import ordination.PN;
import ordination.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTests {

    private Patient patient;
    private Laegemiddel laegemiddel;
    private PN pn;
    private IllegalArgumentException exception;

    @BeforeEach
    void setUp() {
        patient = new Patient("123456-7890", "Peter Ingemann", 160);
        laegemiddel = new Laegemiddel(
                "Acetylsalicylsyre",
                0.1,
                0.2,
                0.3,
                "Styk");

        pn = Controller.opretPNOrdination(
                LocalDate.of(2026,1,01),
                LocalDate.of(2026,10,01),
                patient,
                laegemiddel,
                4
        );
    }

    @AfterEach
    void printException() {
        if (exception != null) {
            System.out.println("Printet fejlmeddelelse: " + "'" + exception.getMessage() + "'");
            exception = null;
        }
    }

    // TESTS


    @Test
    public void opretPNOrdination_gyldigInput_returnererPN() {
        assertNotNull(pn);
        assertTrue(patient.getOrdinationer().contains(pn));
    }

    @Test
    public void PN_TC2_EdgeCasePeriodeOgAntal() {
        PN edgeCase = Controller.opretPNOrdination(
                LocalDate.of(2026,1,01),
                LocalDate.of(2026,1,01),
                patient,
                laegemiddel,
                0
        );

        assertNotNull(edgeCase);
        assertTrue(patient.getOrdinationer().contains(edgeCase));
    }

    // Ugyldige data
    @Test
    public void PN_TC3_UgyldigAntal() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretPNOrdination(
                LocalDate.of(2026,1,01),
                LocalDate.of(2026,10,01),
                patient,
                laegemiddel,
                -4
            );
        });

        assertTrue(exception.getMessage().contains("Antal må ikke være negativ"));
    }

    @Test
    public void PN_TC4_StartDatoEfterSlutDato() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretPNOrdination(
                LocalDate.of(2026,11,01),
                LocalDate.of(2026,10,01),
                patient,
                laegemiddel,
                4
            );
        });

        assertTrue(exception.getMessage().contains("Slutdato kan ikke være før startdato"));
    }

    @Test
    public void PN_TC5_NullPatient() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretPNOrdination(
                    LocalDate.of(2026,1,01),
                    LocalDate.of(2026,10,01),
                    null,
                    laegemiddel,
                    4
            );
        });

        assertTrue(exception.getMessage().contains("Patient og lægemiddel må ikke være null"));
    }

    @Test
    public void PN_TC6_NullLægemiddel() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretPNOrdination(
                    LocalDate.of(2026,1,01),
                    LocalDate.of(2026,10,01),
                    patient,
                    null,
                    4
            );
        });

        assertTrue(exception.getMessage().contains("Patient og lægemiddel må ikke være null"));
    }

    @Test
//    public void fast









}
