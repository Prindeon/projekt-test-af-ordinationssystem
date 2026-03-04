package ordination;

import java.util.ArrayList;

public class DagligFast {
    private Dosis[] doser = new Dosis[4];

    public Dosis[] getDoser() {
        return doser;
    }

    public void setDosis(Dosis[] dosis) {
        this.doser = dosis;
    }

    public Dosis getDosis(int index){
        return doser[index];
    }
}
