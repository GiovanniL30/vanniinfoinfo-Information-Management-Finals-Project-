package shared.utilityClasses;

import java.awt.*;

public class FontFactory {

    public static Font newPoppinsDefault(int fontSize) {
        return new Font("Poppins", Font.PLAIN, fontSize);
    }



    public static Font newPoppinsBold(int fontSize) {
        return new Font("Poppins", Font.BOLD, fontSize);
    }

}
