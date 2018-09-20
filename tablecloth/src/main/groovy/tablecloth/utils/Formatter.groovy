package tablecloth.utils

import java.text.NumberFormat

class Formatter {

    static String number(BigDecimal num) {
        NumberFormat.getNumberInstance(Locale.getDefault()).format(num)
    }

    static String number(Long num) {
       return number(num as BigDecimal)
    }

}
