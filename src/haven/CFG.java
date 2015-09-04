package haven;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public enum CFG {
    Q_SHOW_SINGLE("ui.q.showsingle", true),
    Q_MAX_SINGLE("ui.q.maxsingle", false);

    private static final String CONFIG_JSON = "config.json";
    private static final Map<Object, Object> cfg;
    private static final Map<String, Object> cache = new HashMap<String, Object>();
    private static final Gson gson;
    private final String path;
    public final Object def;

    static {
	gson = (new GsonBuilder()).setPrettyPrinting().create();
	Map<Object, Object> tmp;
	try {
	    FileInputStream fis = new FileInputStream(Config.getFile(CONFIG_JSON));
	    Type type = new TypeToken<Map<Object, Object>>() {
	    }.getType();
	    tmp = gson.fromJson(Utils.stream2str(fis), type);
	    fis.close();
	} catch(Exception e) {
	    tmp = new HashMap<Object, Object>();
	}
	cfg = tmp;
    }

    CFG(String path, Object def) {
	this.path = path;
	this.def = def;
    }

    public Object val(){
	return CFG.get(this);
    }

    public boolean valb(){
	return CFG.getb(this);
    }

    public void set(Object value){
	CFG.set(this, value);
    }

    public static synchronized Object get(CFG name) {
	if(cache.containsKey(name.path)) {
	    return cache.get(name.path);
	} else {
	    Object value = retrieve(name);
	    cache.put(name.path, value);
	    return value;
	}
    }

    public static boolean getb(CFG name) {
	return (Boolean) get(name);
    }

    @SuppressWarnings("unchecked")
    public static synchronized void set(CFG name, Object value) {
	cache.put(name.path, value);
	String[] parts = name.path.split("\\.");
	int i;
	Object cur = cfg;
	for(i = 0; i < parts.length - 1; i++) {
	    String part = parts[i];
	    if(cur instanceof Map) {
		Map<Object, Object> map = (Map<Object, Object>) cur;
		if(map.containsKey(part)) {
		    cur = map.get(part);
		} else {
		    cur = new HashMap<String, Object>();
		    map.put(part, cur);
		}
	    }
	}
	if(cur instanceof Map) {
	    Map<Object, Object> map = (Map) cur;
	    map.put(parts[parts.length - 1], value);
	}
	store();
    }

    private static synchronized void store() {
	String text = gson.toJson(cfg);
	try {
	    FileWriter fw = new FileWriter(Config.getFile(CONFIG_JSON));
	    fw.write(text);
	    fw.close();
	} catch(IOException ignored) {
	}
    }

    private static Object retrieve(CFG name) {
	String[] parts = name.path.split("\\.");
	Object cur = cfg;
	for(String part : parts) {
	    if(cur instanceof Map) {
		Map map = (Map) cur;
		if(map.containsKey(part)) {
		    cur = map.get(part);
		} else {
		    return name.def;
		}
	    } else {
		return name.def;
	    }
	}
	return cur;
    }
}