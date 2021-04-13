ShowSupports.java /* Preprocessed source code */
/* $use: gfx/fx/bprad */

package haven.res.gfx.fx.msrad;

import java.util.*;
import haven.*;
import haven.render.*;
import haven.MenuGrid.Pagina;
import haven.res.gfx.fx.bprad.*;

/* >spr: MSRad */
public class ShowSupports extends MenuGrid.PagButton {
    public ShowSupports(Pagina pag) {
	super(pag);
    }

    public static class Fac implements Factory {
	public MenuGrid.PagButton make(Pagina pag) {
	    return(new ShowSupports(pag));
	}
    }

    public void use() {
	MSRad.show(!MSRad.show);
    }
}
