package tests;


import com.sun.source.tree.AssertTree;
import controller.Controller;
import ordination.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTests {

    private Patient patient;
    private Laegemiddel laegemiddel;
    private PN pn;
    private DagligFast fast;
    private DagligSkaev skaev;
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

        LocalTime[] klokkeSlet = {
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                LocalTime.of(18, 0)
        };

        double[] antalEnheder = {
                1.0,  // morgen
                2.0,  // middag
                1.5   // aften
        };

        skaev = Controller.opretDagligSkaevOrdination(
                LocalDate.of(2026,1,01),
                LocalDate.of(2026,10,01),
                patient,
                laegemiddel,
                klokkeSlet,
                antalEnheder
        );

        fast = Controller.opretDagligFastOrdination(
                LocalDate.of(2026,1,01),
                LocalDate.of(2026,10,01),
                patient,
                laegemiddel,
                1,
                1,
                1,
                1
        );
    }

    @AfterEach
    void printException() {
        if (exception != null) {
            System.out.println("Printet fejlmeddelelse: " + "'" + exception.getMessage() + "'");
            exception = null;
        }
    }

    // opretPNOrdination
    // Gyldige data
    @Test
    public void opretPNOrdination_gyldigInput_returnererPN() {
        assertNotNull(pn);
        assertTrue(patient.getOrdinationer().contains(pn));
    }

    @Test
    public void opretPNOrdination_nulAntalOgSammeDato_returnererPN() {
        PN edgeCase = Controller.opretPNOrdination(
                LocalDate.of(2026,1,01),
                LocalDate.of(2026,1,01),
                patient,
                laegemiddel,
                0
        );

        assertNotNull(edgeCase);
        assertTrue(patient.getOrdinationer().contains(edgeCase));
        assertEquals(0, edgeCase.getAntalEnheder());
    }

    // Ugyldige data
    @Test
    public void opretPNOrdination_negativAntal_kasterException() {
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
    public void opretPNOrdination_startDatoEfterSlutDato_kasterException() {
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
    public void opretPNOrdination_nullPatient_kasterException() {
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
    public void opretPNOrdination_nullLægemiddel_kasterException() {
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

    // opretDagligFastOrdination
    @Test
    public void opretDagligFastOrdination_gyldigInput_returnererOrdination() {
        assertNotNull(fast);
        assertTrue(patient.getOrdinationer().contains(fast));
    }

    @Test
    public void opretDagligFastOrdination_nulAntal_returnererOrdination() {
        DagligFast edgeCase = Controller.opretDagligFastOrdination(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 1),
                patient,
                laegemiddel,
                0,
                0,
                0,
                0
        );

        assertNotNull(edgeCase);
        assertTrue(patient.getOrdinationer().contains(edgeCase));
        assertEquals(0, edgeCase.doegnDosis());
    }

    // Bare for at verificere, den gør som den skal
    @Test
    public void opretDagligFastOrdination_gyldigInput_korrektDoegnDosis() {
        assertEquals(4, fast.doegnDosis());
    }

    // Ugyldige data
    @Test
    public void opretDagligFastOrdination_startDatoEfterSlutDato_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligFastOrdination(
                    LocalDate.of(2026, 11, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    1,
                    1,
                    1,
                    1
            );
        });

        assertTrue(exception.getMessage().contains("Slutdato kan ikke være før startdato"));
    }

    @Test
    public void opretDagligFastOrdination_nullPatient_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligFastOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    null,
                    laegemiddel,
                    1,
                    1,
                    1,
                    1
            );
        });

        assertTrue(exception.getMessage().contains("Patient og lægemiddel må ikke være null"));
    }

    @Test
    public void opretDagligFastOrdination_nullLægemiddel_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligFastOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    null,
                    1,
                    1,
                    1,
                    1
            );
        });

        assertTrue(exception.getMessage().contains("Patient og lægemiddel må ikke være null"));
    }

    @Test
    public void opretDagligFastOrdination_negativAntal_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligFastOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    -1,
                    0,
                    0,
                    0
            );
        });

        assertTrue(exception.getMessage().contains("Antal må ikke være negativ"));
    }

    @Test
    public void opretDagligFastOrdination_negativMiddagAntal_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligFastOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    0,
                    -1,
                    0,
                    0
            );
        });

        assertTrue(exception.getMessage().contains("Antal må ikke være negativ"));
    }

    @Test
    public void opretDagligFastOrdination_negativAftenAntal_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligFastOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    0,
                    0,
                    -1,
                    0
            );
        });

        assertTrue(exception.getMessage().contains("Antal må ikke være negativ"));
    }

    @Test
    public void opretDagligFastOrdination_negativNatAntal_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligFastOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    0,
                    0,
                    0,
                    -1
            );
        });

        assertTrue(exception.getMessage().contains("Antal må ikke være negativ"));
    }

    // opretDagligSkaevOrdination
    // Gyldige data
    @Test
    public void opretDagligSkaev_gyldigInput_returnererOrdination() {
        assertNotNull(skaev);
        assertTrue(patient.getOrdinationer().contains(skaev));
    }

    @Test
    public void opretDagligSkaev_gyldigInput_korrektDoegnDosis() {
        assertEquals(4.5, skaev.doegnDosis());
    }

    // Ugyldige data
    @Test
    public void opretDagligSkaev_startDatoEfterSlutDato_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligSkaevOrdination(
                    LocalDate.of(2026, 11, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    new LocalTime[]{LocalTime.of(8, 0)},
                    new double[]{1.0}
            );
        });

        assertTrue(exception.getMessage().contains("Slutdato kan ikke være før startdato"));
    }

    @Test
    public void opretDagligSkaev_nullPatient_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligSkaevOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    null,
                    laegemiddel,
                    new LocalTime[]{LocalTime.of(8, 0)},
                    new double[]{1.0}
            );
        });

        assertTrue(exception.getMessage().contains("Patient eller lægemiddel må ikke være null"));
    }

    @Test
    public void opretDagligSkaev_nullLægemiddel_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligSkaevOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    null,
                    new LocalTime[]{LocalTime.of(8, 0)},
                    new double[]{1.0}
            );
        });

        assertTrue(exception.getMessage().contains("Patient eller lægemiddel må ikke være null"));
    }

    @Test
    public void opretDagligSkaev_nullKlokkeSlet_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligSkaevOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    null,
                    new double[]{1.0}
            );
        });

        assertTrue(exception.getMessage().contains("Klokkeslet og antal må ikke være null"));
    }

    @Test
    public void opretDagligSkaev_nullAntalEnheder_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligSkaevOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    new LocalTime[]{LocalTime.of(8, 0)},
                    null
            );
        });

        assertTrue(exception.getMessage().contains("Klokkeslet og antal må ikke være null"));
    }

    @Test
    public void opretDagligSkaev_forskelligArrayLængde_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligSkaevOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    new LocalTime[]{LocalTime.of(8, 0), LocalTime.of(12, 0)},
                    new double[]{1.0}
            );
        });

        assertTrue(exception.getMessage().contains("Alle enheder skal have 1 klokkeslet"));
    }

    @Test
    public void opretDagligSkaev_nulAntalEnheder_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligSkaevOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    new LocalTime[]{LocalTime.of(8, 0)},
                    new double[]{0}
            );
        });

        assertTrue(exception.getMessage().contains("Alle enheder skal være større end 0"));
    }

    @Test
    public void opretDagligSkaev_negativAntalEnheder_kasterException() {
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.opretDagligSkaevOrdination(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 10, 1),
                    patient,
                    laegemiddel,
                    new LocalTime[]{LocalTime.of(8, 0)},
                    new double[]{-1.0}
            );
        });

        assertTrue(exception.getMessage().contains("Alle enheder skal være større end 0"));
    }












}
