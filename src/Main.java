import storage.FileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String args[]){
        final Path dataDir   = Paths.get("data");
        final Path usersFile = dataDir.resolve("users.csv");
        final Path ticketsFile = dataDir.resolve("tickets.csv");

        FileManager fileManager = new FileManager();
        fileManager.readCsv(usersFile.);
    }

    //PANELS:
    void openingPanel(){
        System.out.println("1) Login\n2) Register\n3) Exit");
        //get user & pas,
        //core.Admin or core.User? return its panel
    }

    void adminPanel(){
        System.out.println("1) Support Tickets\n2) Mange System\n3)Log out");
    }

    void ticketSupportPanel(){
        System.out.println("1) View All Tickets\n2) Process Next\n3) Change Status\n4) Undo\n");
    }

    void systemManagementPanel(){
        System.out.println("1) Sort Tickets\n2) Search core.User\n3) Delete core.User\n4) Add core.Admin\n5) Change System Time");
    }

    void userPanel(){
        System.out.println("1) Register\n2) Create models.Ticket\n3) My Tickets\n4) Search models.Ticket");
    }

    void ChangeSystemTime(){

    }
}
