package ru.sbsoft.dao;

/**
 *
 * @author Kiselev
 */
public class DbTestConfig {
    public static String getDriver(){
        return "org.h2.Driver";
    }
    
    public static String getUrl(){
        return"jdbc:h2:~/test";
    }
    
    public static String getLogin(){
        return null;
    }
            
    public static String getPassw(){
        return null;
    }
}
