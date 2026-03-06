package tests;


import com.sun.source.tree.AssertTree;
import controller.Controller;
import ordination.Laegemiddel;
import ordination.Ordination;
import ordination.PN;
import ordination.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTests {

    private Patient patient;
    private Laegemiddel laegemiddel;
    private PN pn;

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
                LocalDate.of(2026,01,01),
                LocalDate.of(2026,10,01),
                patient,
                laegemiddel,
                4
        );

    }

    @Test
    public void TC1_gyldigPeriodeOgAntal() {
        assertNotNull(pn);
        assertTrue(patient.getOrdinationer().contains(pn));
    }

    @Test
    public void TC2_EdgeCasePeriodeOgAntal() {
        PN edgeCase = Controller.opretPNOrdination(
                LocalDate.of(2026,01,01),
                LocalDate.of(2026,01,01),
                patient,
                laegemiddel,
                0
        );

        assertNotNull(edgeCase);
        assertTrue(patient.getOrdinationer().contains(edgeCase));
    }

    // Ugyldige data
    @Test
    public void TC3_UgyldigAntal() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretPNOrdination(
                LocalDate.of(2026,01,01),
                LocalDate.of(2026,10,01),
                patient,
                laegemiddel,
                -4
            );
        });

        assertTrue(e.getMessage().contains("Antal må ikke være negativ"));
    }

    @Test
    public void TC4_StartDatoEfterSlutDato() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretPNOrdination(
                LocalDate.of(2026,11,01),
                LocalDate.of(2026,10,01),
                patient,
                laegemiddel,
                4
            );
        });

        assertTrue(e.getMessage().contains("Slutdato kan ikke være før startdato"));
    }




}
