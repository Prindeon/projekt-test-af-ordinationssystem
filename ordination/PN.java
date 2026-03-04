package ordination;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class PN extends Ordination {

    private double antalEnheder;
    private ArrayList<LocalDate> dageGivet = new ArrayList<>();

    /**
     * Registrerer at der er givet en dosis paa dagen givesDen
     * Returnerer true hvis givesDen er inden for ordinationens gyldighedsperiode og datoen huskes
     * Retrurner false ellers og datoen givesDen ignoreres
     * @paramgivesDen
     * @return
     */
    public PN(LocalDate startDen, LocalDate slutDen, Laegemiddel laegemiddel, double antalEnheder) {
        super(startDen, slutDen, laegemiddel);
        this.antalEnheder = antalEnheder;
    }

    public boolean givDosis(LocalDate givesDen) {
        boolean returnn;

        if (givesDen == null) {
            throw new IllegalArgumentException("Dato må ikke være null");
        }

        if (givesDen.isBefore(getStartDen()) || givesDen.isAfter(getStartDen())) {
            returnn = false;
        }

        dageGivet.add(givesDen);
        returnn = true;
        return returnn;
    }

    @Override
    public double doegnDosis() {
        double output;

        if (!dageGivet.isEmpty()) {

            LocalDate første = dageGivet.get(0);
            LocalDate sidste = dageGivet.get(0);

            // Kører igennem alle datoer
            // Finder og sætter den første og sidste, hvor en dosis er givet
            for (LocalDate dato : dageGivet) {
                if (dato.isBefore(første)) {
                    første = dato;
                }
                if (dato.isAfter(sidste)) {
                    sidste = dato;
                }
            }

            long dage = ChronoUnit.DAYS.between(første, sidste) + 1;
            output = samletDosis() / dage;
        }  else {
            output = 0;
        }

        return output;
    }

    @Override
    public double samletDosis() {
        return dageGivet.size() * antalEnheder;
    }

    @Override
    public String getType() {
        return "PN";
    }

    /**
     * Returnerer antal gange ordinationen er anvendt
     * @return
     */
    public int getDageGivet() {
        return dageGivet.size();
    }

    public double getAntalEnheder() {
        return antalEnheder;
    }
}
