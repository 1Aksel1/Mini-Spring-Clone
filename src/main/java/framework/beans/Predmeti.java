package framework.beans;

import framework.annotations.Autowired;
import framework.annotations.Service;

@Service
public class Predmeti {

    @Autowired(verbose = true)
    public NWP nwp;

    public Predmeti() {
    }


}
