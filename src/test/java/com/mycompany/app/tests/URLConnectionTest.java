package com.mycompany.app.tests;
import java.net.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*; // test assertions

/* Learning tests */
public class URLConnectionTest
{
    @Test
    public void test_urlconnection_status ()
    {
        try
        {
            URLConnection conn = (new URL("http://google.com")).openConnection();
            String status_line = conn.getHeaderField(0);
            String[] parts = status_line.split(" ");
            try
            {
                int status = Integer.parseInt(parts[1]);
            }
            catch (NumberFormatException e)
            {
                fail("Couldn't get the status code");
            }
        }
        catch (Exception e)
        {
            System.out.println("exc: ");
            e.printStackTrace();
        }
    }
}

