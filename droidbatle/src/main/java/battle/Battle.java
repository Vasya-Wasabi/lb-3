package battle;
import droids.Droid;
import java.util.Scanner;

public class Battle {
    private static final Scanner scanner = new Scanner(System.in);


    protected String collectTurnLog(Droid performer, Droid target) {
        boolean skillAllowed = performer.isCanUseSkill();

        String prompt = "Choose action for " + performer.getName() + ": 1) Attack ";
        if(skillAllowed) {
            prompt +=  "2) Skill ";
        }
        prompt +=  "-> ";

        System.out.print(prompt);
        int choice = scanner.nextInt();

        if(choice == 2 && !skillAllowed) {
            System.out.println("Skill is not allowed. Automatically attack!");
            choice = 1;
        }

        String actionDescription = executeActionAndGetDescription(performer, target, choice);

        return buildActionLog(performer, target, actionDescription);
    }

    private String executeActionAndGetDescription(Droid performer, Droid target, int choice) {
        switch (choice) {
            case 1:
                performer.attack(target);
                performer.setCanUseSkill(true);
                return " DMG: " + performer.getDamage();
            case 2:
                performer.skill();
                performer.setCanUseSkill(false);
                return " SKILL  ";
            default:
                return " Invalid choice. Skipping turn!";
        }
    }

    protected  String buildActionLog(Droid performer, Droid target, String actionDescription) {
        String actionHeader = "| " + performer.getName() + " -> "
                + target.getName() + " | ";

        String actionPart = actionDescription + " | ";

        String statePart = performer + "; " + target + " |";

        return actionHeader + actionPart + statePart;
    }
}
