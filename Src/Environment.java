import java.util.HashMap;
import java.util.Map;

public class Environment {
    //private means only the Environment class itself can access this map directly
    // To keep track of the variables and Non static beacuse each environment should be independent
    //String as key and any it value can be anything like number, integer that's why we use object
    private Map<String, Object> variables = new HashMap<>();
    //Set function for assignment of the variables 
    public void set(String name, Object value) {
        variables.put(name, value);
    }
    //get is called when a variable is used in an expression, e.g. >> x
    public Object get(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Variable not defined: " + name);
        }
        return variables.get(name);
    }


}