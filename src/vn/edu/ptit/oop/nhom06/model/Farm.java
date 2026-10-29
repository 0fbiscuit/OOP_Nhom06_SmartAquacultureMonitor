package vn.edu.ptit.oop.nhom06.model;

import java.util.ArrayList;
import java.util.List;

public class Farm {
    private String farmName;
    private String address;

    private List<Pond> ponds;

    public Farm(String farmName, String address) {
        this.farmName = farmName;
        this.address = address;
        this.ponds = new ArrayList<>();
    }

    public void addPond(Pond pond) {
        if (pond != null) {
            this.ponds.add(pond);
        }
    }

    public List<Pond> getPonds() {
        return java.util.Collections.unmodifiableList(ponds);
    }

    public Pond getPondById(String pondId) {
        for (Pond p : ponds) {
            if (p.getPondId().equals(pondId)) {
                return p;
            }
        }
        return null;
    }
}
