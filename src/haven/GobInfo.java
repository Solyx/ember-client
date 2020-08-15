package haven;

import haven.render.Homo3D;
import haven.render.Pipe;
import haven.render.RenderTree;

public abstract class GobInfo extends GAttrib implements RenderTree.Node, PView.Render2D {
    protected Tex tex;
    protected int up = 1;
    protected final Object texLock = new Object();
    protected Pair<Double, Double> center = new Pair<>(0.5, 0.5);
    
    public GobInfo(Gob owner) {
	super(owner);
    }
    
    protected abstract boolean enabled();
    
    @Override
    public void ctick(double dt) {
	synchronized (texLock) {
	    if(enabled() && tex == null) {
		tex = render();
	    }
	}
    }
    
    @Override
    public void draw(GOut g, Pipe state) {
	synchronized (texLock) {
	    if(enabled() && tex != null) {
		Coord sc = Homo3D.obj2view(new Coord3f(0, 0, up), state, Area.sized(g.sz())).round2();
		if(sc.isect(Coord.z, g.sz())) {
		    g.aimage(tex, sc, center.a, center.b);
		}
	    }
	}
    }
    
    protected abstract Tex render();

    protected void clean() {
        synchronized(texLock) {
	    if(tex != null) {
		tex.dispose();
		tex = null;
	    }
	}
    }

    public void dispose() {
	clean();
    }
}
