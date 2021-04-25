package com.monk.demo.lambda;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

interface Description {
    String brief();
}

interface Body {
    String detailed(String head);
}

interface Multi {
    String twoArg(String head, Double d);
}

interface IntCall {
    int call(int arg);
  }

public class LambdaExpressions {

    static Body bod = h -> h + " No Parens!"; // [1]

    static Body bod2 = (h) -> h + " More details"; // [2]

    static Description desc = () -> "Short info"; // [3]

    static Multi mult = (h, n) -> h + n; // [4]

    static Description moreLines = () -> { // [5]
        System.out.println("moreLines()");
        return "from moreLines()";
    };
    
    static IntCall fact;

    public static void main(String[] args) {
        System.out.println(bod.detailed("Oh!"));
        System.out.println(bod2.detailed("Hi!"));
        System.out.println(desc.brief());
        System.out.println(mult.twoArg("Pi! ", 3.14159));
        System.out.println(moreLines.brief());
        
        fact = n -> n == 0 ? 1 : n * fact.call(n - 1);
        for(int i = 0; i <= 10; i++) {
            System.out.println(fact.call(i));
        }
        
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.now();
        System.out.println(date.format(dtf));
    }
}
