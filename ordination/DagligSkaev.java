package ordination;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class DagligSkaev extends Ordination {

    private final ArrayList<Dosis> doser = new ArrayList<>();

    public DagligSkaev(LocalDate startDen, LocalDate slutDen, Laegemiddel laegemiddel) {
        super(startDen, slutDen, laegemiddel);
    }

    public ArrayList<Dosis> getDoser() {
        return doser;
    }


    public void opretDosis(LocalTime tid, double antal) {
        if (tid == null) {
            throw new IllegalArgumentException("Tid må ikke være null");
        }
        if (antal < 0) {
            throw new IllegalArgumentException("Antal må ikke være negativ");
        }
        doser.add(new Dosis(tid, antal));
    }

    @Override
    public double doegnDosis() {
        double sum = 0;
        for (Dosis d : doser) {
            if (d != null) {
                sum += d.getAntal();
            }
        }
        return sum;
    }

    @Override
    public double samletDosis() {
        return doegnDosis() * antalDage();
    }

    @Override
    public String getType() {
        return "DagligSkæv";
    }
}
