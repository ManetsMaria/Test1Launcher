package com.example.marya.firsttestingproject;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doReturn;

/**
 * Created by marya on 21.5.17.
 */

@RunWith(RobolectricTestRunner.class)
public class ProgrammTest {
    private Programm programm = new Programm("name","label",0);
    private Programm programm2= new Programm("name2","label",5);
    @Test
    public void testGetCount() {
        List<Programm> list = mock(ArrayList.class);
        doReturn(programm).when(list).get(anyInt());
        assertThat(list.get(0).getCount()).isEqualTo(0);
        //assertEquals(,0);
    }
    @Test
    public void testGetName() {
        List<Programm> list = mock(ArrayList.class);
        doReturn(programm).when(list).get(anyInt());
        assertEquals(list.get(0).getName(),"name");
    }
    @Test
    public void testGetLabel() {
        List<Programm> list = mock(ArrayList.class);
        doReturn(programm).when(list).get(anyInt());
        assertEquals(list.get(0).getLabel(),"label");
    }
    @Test
    public void testGetPhoto() {
        List<Programm> list = mock(ArrayList.class);
        doReturn(programm).when(list).get(anyInt());
        assertEquals(list.get(0).getPhotoId(),null);
    }
    @Test
    public void testToString() {
        List<Programm> list = mock(ArrayList.class);
        doReturn(programm).when(list).get(anyInt());
        assertEquals(list.get(0).toString(),"name 0");
    }
    @Test
    public void testEquals() {
        assertEquals(programm.equals(programm2),true);
    }

    @Test
    public void testCompareTo() {
        assertEquals(programm2.compareTo(programm)>0,true);
    }
}
