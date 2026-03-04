package ordination;


import java.time.LocalDate;

public class DagligFast extends Ordination {

    // 0 = morgen, 1 = middag, 2 = aften, 3 = nat
    private Dosis[] doser = new Dosis[4];

    public DagligFast(LocalDate startDen, LocalDate slutDen, Laegemiddel laegemiddel) {
        super(startDen, slutDen, laegemiddel);
    }
    public Dosis[] getDoser() {
        return doser;
    }

    public void setDosis(int index, Dosis dosis) {
        if (index < 0 || index >= doser.length) {
            throw new IllegalArgumentException("Index skal være mellem 0 og 3");
        }
        doser[index] = dosis;
    }

    public Dosis getDosis(int index) {
        if (index < 0 || index >= doser.length) {
            throw new IllegalArgumentException("Ugyldigt index");
        }
        return doser[index];
    }

    @Override
    public double doegnDosis() {
        double sum = 0;

        for (Dosis dosis : doser) {
            if (dosis != null) {
                sum += dosis.getAntal();
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
        return "DagligFast";
    }




}
