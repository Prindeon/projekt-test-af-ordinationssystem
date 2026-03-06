package ordination;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
// o
public abstract class Ordination {
    private LocalDate startDen;
    private LocalDate slutDen;

    private Laegemiddel laegemiddel;

    public Ordination(LocalDate startDen, LocalDate slutDen, Laegemiddel laegemiddel) {
        if (startDen == null || slutDen == null) {
            throw new IllegalArgumentException("Start og slutdato må ikke være null");
        }

        if (laegemiddel == null) {
            throw new IllegalArgumentException("Lægemiddel må ikke være null");
        }

        if (slutDen.isBefore(startDen)) {
            throw new IllegalArgumentException("Slutdato må ikke være før startdato");
        }

        this.startDen = startDen;
        this.slutDen = slutDen;
        this.laegemiddel = laegemiddel;
    }

    public LocalDate getStartDen() {
        return startDen;
    }	

    public LocalDate getSlutDen() {
        return slutDen;
    }

    public Laegemiddel getLaegemiddel() {
        return laegemiddel;
    }

    /**
     * Antal hele dage mellem startdato og slutdato. Begge dage inklusive.
     * @return antal dage ordinationen gælder for
     */
    public int antalDage() {
        return (int) ChronoUnit.DAYS.between(startDen, slutDen) + 1;
    }

    @Override
    public String toString() {
        return startDen.toString();
    }

    /**
     * Returnerer den totale dosis der er givet i den periode ordinationen er gyldig
     * @return
     */
    public abstract double samletDosis();

    /**
     * Returnerer den gennemsnitlige dosis givet pr dag i den periode ordinationen er gyldig
     * @return
     */
    public abstract double doegnDosis();

    /**
     * Returnerer ordinationstypen som en String
     * @return
     */
    public abstract String getType();


    public void setStartDen(LocalDate startDen) {
        this.startDen = startDen;
    }

    public void setSlutDen(LocalDate slutDen) {
        this.slutDen = slutDen;
    }

    public void setLaegemiddel(Laegemiddel laegemiddel) {
        this.laegemiddel = laegemiddel;
    }
}
