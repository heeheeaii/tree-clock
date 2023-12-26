package com.treevalue.clock;

import java.time.LocalTime;

public class GetCurrentTime {
    static public void f(Boolean b){
        b = true;
    }

    public static void main(String[] args) {
        Boolean a = false;
        f(a);
        System.out.println(a); // This will print: false
    }

}
