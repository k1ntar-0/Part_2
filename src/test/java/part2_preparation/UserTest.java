/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package part2_preparation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class UserTest {
    
    public UserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class User.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        User expResult = null;
        User result = User.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCredentials method, of class User.
     */
    @Test
    public void testSetCredentials() {
        System.out.println("setCredentials");
        String username = "Kint_";
        String password = "Kintar0_yuske";
        User instance = new User();
        instance.setCredentials(username, password);
        
    }

    /**
     * Test of checkCredentials method, of class User.
     */
    @Test
    public void testCheckCredentials() {
        System.out.println("checkCredentials");
        String username = "Kint_";
        String password = "Kintar0_yuske";
        User instance = new User();
        boolean expResult = true;
        boolean result = instance.checkCredentials(username, password);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of setFirstName method, of class User.
     */
    @Test
    public void testSetFirstName() {
        System.out.println("setFirstName");
        String firstName = "kin";
        User instance = new User();
        instance.setFirstName(firstName);
        
    }

    /**
     * Test of getFirstName method, of class User.
     */
    @Test
    public void testGetFirstName() {
        System.out.println("getFirstName");
        User instance = new User();
        String expResult = "Kint_";
        String result = instance.getFirstName();
        assertEquals(expResult, result);
        
    }
    
}
