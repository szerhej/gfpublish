package fg.dev.car.bo;

/**
 * Usage types
 */
public enum Usage {
    DOMESTIC,FOREIGN;

    /**
     * Resolve value from parameter
     * Returns Null if parameter is unknown
     * @param txt Text representation of value
     * @return
     */
    public static Usage resolve(String txt){
        try {
            return Usage.valueOf(txt.trim().toUpperCase());
        }catch (Exception e){
            return null;
        }
    }


}
