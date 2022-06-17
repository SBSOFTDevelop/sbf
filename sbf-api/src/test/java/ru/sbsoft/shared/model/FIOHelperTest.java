package ru.sbsoft.shared.model;

import ru.sbsoft.shared.model.FIOHelper;
import org.junit.Assert;
import org.junit.Test;

public class FIOHelperTest {
    
    public FIOHelperTest() {
    }
    
    @Test
    public void testGetEmptyFullName() {
        System.out.println("getEmptyFullName");
        String surName = "";
        String firstName = "";
        String middleName = "";
        String expResult = "";
        String result = FIOHelper.getFullName(surName, firstName, middleName);
        Assert.assertEquals(expResult, result);
    }
    
    @Test
    public void testGetFullName() {
        System.out.println("getEmptyFullName");
        String surName = "Иванов";
        String firstName = "Иван";
        String middleName = "Иванович";
        String expResult = "Иванов Иван Иванович";
        String result = FIOHelper.getFullName(surName, firstName, middleName);
        Assert.assertEquals(expResult, result);
    }
   
    @Test
    public void testGetNotPatronimicFullName() {
        System.out.println("getEmptyFullName");
        String surName = "Иванов";
        String firstName = "Иван";
        String middleName = null;
        String expResult = "Иванов Иван";
        String result = FIOHelper.getFullName(surName, firstName, middleName);
        Assert.assertEquals(expResult, result);
    }
   
    @Test
    public void testGetIOFullName() {
        System.out.println("getEmptyFullName");
        String surName = null;
        String firstName = "Иван";
        String middleName = "Иванович";
        String expResult = "Иван Иванович";
        String result = FIOHelper.getFullName(surName, firstName, middleName);
        Assert.assertEquals(expResult, result);
    }
}
