package com.example.marya.firsttestingproject;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by marya on 21.5.17.
 */

public class ContactTest {
    Contact contact=new Contact(1,"name", "phone");
    Contact contact2=new Contact (0, "name2", "phone2");
    @Test
    public void testGetPhone(){
        Assert.assertEquals(contact.getPhone(),"phone");
    }
    @Test
    public void testGetName(){
        Assert.assertEquals(contact.getName(),"name");
    }
    @Before
    public void setName(){
        contact2.setName("name");
    }
    @Test
    public void testEquals(){
        Assert.assertEquals(contact.equals(contact2),true);
    }
}
