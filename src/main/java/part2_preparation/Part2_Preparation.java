
package part2_preparation;

/**
 *St number:10487456
 * St name:Ndaedzo Tshiovhe
 * Assignment part 2
 * @author Kintaro
 */
public class Part2_Preparation {

   public static void main(String[] args) {
        Registration registration = new Registration();
         Login login = new Login(registration);
        login.setVisible(true);
                
    }
}
