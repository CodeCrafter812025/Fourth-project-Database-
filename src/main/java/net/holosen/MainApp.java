package net.holosen;

import static net.holosen.handlers.dbhandler.CapacityHandler.loadCapacity;
import static net.holosen.handlers.dbhandler.CapacityHandler.showCapacity;
import static net.holosen.handlers.dbhandler.StudentHandler.*;
import static net.holosen.utils.IOUtils.getInput;
import static net.holosen.utils.IOUtils.print;
import static net.holosen.utils.MenuUtil.showMenu;

public class MainApp {
    private static Integer capacity = 0;

    public static void main(String[] args) {
        capacity = loadCapacity();
        while (true) {
            try {
                showMenu();
                Integer menu = Integer.parseInt(getInput());
                switch (menu) {
                    case 1:
                        registerStudent();
                        break;
                    case 2:
                        showCapacity(capacity);
                        break;
                    case 3:
                        updateStudentData();
                        break;
                    case 4:
                        cancelRegistration();
                        break;
                    case 5:
                        showStudentInformation();
                        break;
                    case 0:
                        print("GoodBye!");
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
