package fg.dev.car.bo;

public enum Usage {
    DOMESTIC,FOREIGN;

    public static Usage resolve(String txt){
        try {
            return Usage.valueOf(txt.trim().toUpperCase());
        }catch (Exception e){
            return null;
        }
    }


}
